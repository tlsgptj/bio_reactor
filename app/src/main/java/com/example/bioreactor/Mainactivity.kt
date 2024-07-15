package com.example.bioreactor

import MqttClientManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var UV_button : Button
    private lateinit var Video: VideoView
    private lateinit var time: TextView
    private lateinit var recycler_result: RecyclerView
    private lateinit var motor_text1: EditText
    private lateinit var motor_text2: EditText
    private lateinit var recycler_chart: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var dataRef: DatabaseReference
    private lateinit var dataUV : DatabaseReference
    private lateinit var datatime : DatabaseReference
    private var isUVON = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        //초기화
        UV_button = UV_button.findViewById<Button>(R.id.UV)
        Video = Video.findViewById<VideoView>(R.id.Video)
        time = time.findViewById<TextView>(R.id.time)
        motor_text1 = motor_text1.findViewById<EditText>(R.id.motor_text1)
        motor_text2 = motor_text2.findViewById<EditText>(R.id.motor_text2)

        UV_button.setOnClickListener {
            //버튼은 on, off만 될 수 있도록 설정해야함
            if (isUVON) {
                turnOffUV()
            } else {
                turnONUV()
            }
            isUVON = !isUVON
        }


        // Firebase 데이터베이스 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        dataRef = database.getReference("temp") // "data" 경로를 가리키는 데이터베이스 참조
        dataUV = database.getReference("UV")
        datatime = database.getReference("time")


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


        // HTTP 요청 및 데이터 전송
        sendHttpRequest()
    }
    private fun turnONUV() {


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

