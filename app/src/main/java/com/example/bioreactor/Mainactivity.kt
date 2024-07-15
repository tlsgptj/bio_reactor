package com.example.bioreactor

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var dataPH : DatabaseReference
    private lateinit var dataMotor1 : DatabaseReference
    private lateinit var dataID : DatabaseReference
    private lateinit var dataPW : DatabaseReference
    private lateinit var dataMotor2 : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        //초기화
        UV_button = UV_button.findViewById<Button>(R.id.UV)
        Video = Video.findViewById<VideoView>(R.id.Video)
        time = time.findViewById<TextView>(R.id.time)
        motor_text1 = motor_text1.findViewById<EditText>(R.id.motor_text1)
        motor_text2 = motor_text2.findViewById<EditText>(R.id.motor_text2)


        // Firebase 데이터베이스 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        dataRef = database.getReference("temp") // "data" 경로를 가리키는 데이터베이스 참조
        dataUV = database.getReference("UV")
        datatime = database.getReference("time")
        dataPH = database.getReference("PH")
        dataID = database.getReference("ID")
        dataPW = database.getReference("PW")
        dataMotor1 = database.getReference("motor")
        dataMotor2 = database.getReference("motor1")

        UV_button.setOnClickListener {
            //버튼은 on, off만 될 수 있도록 설정해야함
            dataUV.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val isUVOn = dataSnapshot.getValue(Boolean::class.java) ?: false

                    // UV 상태에 따라 동작을 수행
                    if (isUVOn) {
                        // UV가 켜져 있는 경우
                        //색깔 바뀌는 함수 추가
                        dataUV.setValue("false")
                    } else {
                        // UV가 꺼져 있는 경우
                        dataUV.setValue("true")
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("failed", "실패요~~~~~")
                }
            })
        }


        // 온도를 데이터에 받아와서 저장합니다.
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
    //모터 제어 함수
    private fun saveTextToDatabase(reference: DatabaseReference, text: String, context: Context) {
        val key = reference.push().key
        if (key != null) {
            reference.setValue(text)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "데이터가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "데이터 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // 특정 데이터베이스 참조에 텍스트 저장 함수 1
    private fun saveTextToDatabase1(text: String, context: Context) {
        saveTextToDatabase(dataMotor1, text, context)
    }

    // 특정 데이터베이스 참조에 텍스트 저장 함수 2
    private fun saveTextToDatabase2(text: String, context: Context) {
        saveTextToDatabase(dataMotor2, text, context)
    }

    private fun realTimeClock() {
        //시간 로직 구현


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
                    data.push().setValue(mapOf("temperature" to temperature))
                }
            }
        })
    }



