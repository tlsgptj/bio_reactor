package com.example.bioreactor

import MqttClientManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Firebase 데이터베이스 인스턴스 및 데이터베이스 참조
    private lateinit var database: FirebaseDatabase
    private lateinit var dataRef: DatabaseReference
    private lateinit var mqttClientManager: MqttClientManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        //서버 경로 추가
        mqttClientManager = MqttClientManager(this, "tcp://broker-url:1883", "android-client-id")
        //MQTT 테스트
        mqttClientManager.connect(object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                mqttClientManager.subscribe("myTopic", 1)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("fail", "failed")
            }

        })
        // Firebase 데이터베이스 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        dataRef = database.getReference("data") // "data" 경로를 가리키는 데이터베이스 참조

        // 데이터 읽기를 위한 ValueEventListener 설정
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터가 변경될 때 호출됨
                for (snapshot in dataSnapshot.children) {
                    val sensorData = snapshot.getValue(SensorData::class.java)
                    // 온도 데이터 추출 및 처리
                    sensorData?.let {
                        val temperatures = listOfNotNull(
                            it.temp1, it.temp2, it.temp3, it.temp4, it.temp5,
                            it.temp6, it.temp7, it.temp8, it.temp9, it.temp10
                        )
                        Log.d(TAG, "Temperatures: $temperatures")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 읽기가 실패한 경우 호출됨
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }

        // ValueEventListener를 데이터베이스 참조에 추가하여 데이터를 실시간으로 가져옴
        dataRef.addValueEventListener(valueEventListener)

        // MQTT 연결 및 데이터 수신
        setupMqtt()

        // HTTP 요청 및 데이터 전송
        sendHttpRequest()
    }

    private fun setupMqtt() {
        val clientId = MqttClient.generateClientId()
        val mqttClient = MqttAndroidClient(this.applicationContext, "tcp://broker.hivemq.com:1883", clientId)

        try {
            mqttClient.connect().actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d(TAG, "Connected to MQTT broker")

                    mqttClient.subscribe("sensor/temperature", 0, object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken) {
                            Log.d(TAG, "Subscribed to topic")
                        }

                        override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                            Log.e(TAG, "Failed to subscribe to topic", exception)
                        }
                    })

                    mqttClient.setCallback(object : MqttCallback {
                        override fun connectionLost(cause: Throwable?) {
                            Log.e(TAG, "Connection lost", cause)
                        }

                        override fun messageArrived(topic: String, message: MqttMessage) {
                            val temperature = message.toString()
                            Log.d(TAG, "Message arrived: $temperature")
                            dataRef.push().setValue(mapOf("temperature" to temperature))

                            val data = HashMap<String, Any>()
                            data["temperature"] = temperature
                            dataRef.push().setValue((data))
                        }

                        override fun deliveryComplete(token: IMqttDeliveryToken) {
                            // No action needed
                        }
                    })
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e(TAG, "Failed to connect to MQTT broker", exception)
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun sendHttpRequest() {
        val client = OkHttpClient()
        val url = "https://api.example.com/sensor"

        val json = JSONObject()
        json.put("temperature", 25)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "HTTP request failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val responseData = it.string()
                    val jsonResponse = JSONObject(responseData)
                    val temperature = jsonResponse.getInt("temperature")
                    dataRef.push().setValue(mapOf("temperature" to temperature))
                }
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

