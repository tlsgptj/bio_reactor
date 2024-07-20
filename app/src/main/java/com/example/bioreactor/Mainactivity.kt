package com.example.bioreactor

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), Runnable {
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
    private lateinit var dataMotor2 : DatabaseReference
    private var handler = Handler()
    private lateinit var resultAdapter: ViewAdapter
    private lateinit var chartViewAdapter: chartViewAdapter
    private lateinit var chartViewHolder: chartViewAdapter.ChartViewHolder
    private lateinit var runnable : Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        // 초기화
        UV_button = findViewById(R.id.UV)
        Video = findViewById(R.id.Video)//안됨
        time = findViewById(R.id.time)
        motor_text1 = findViewById(R.id.motor_text1)
        motor_text2 = findViewById(R.id.motor_text2)
        recycler_result = findViewById<RecyclerView>(R.id.recycler_result)
        recycler_chart = findViewById<RecyclerView>(R.id.recycler_chart)

        // Firebase 데이터베이스 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        dataRef = database.getReference("temp")
        dataUV = database.getReference("UV")
        datatime = database.getReference("time")
        dataPH = database.getReference("PH")
        dataMotor1 = database.getReference("motor")
        dataMotor2 = database.getReference("motor1")


        UV_button.setOnClickListener {
            dataUV.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val isUVOn = dataSnapshot.getValue(Boolean::class.java) ?: false
                    if (isUVOn) {
                        dataUV.setValue("false")
                    } else {
                        dataUV.setValue("true")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("failed", "실패요~~~~~")
                }
            })
        }

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chartDataList = mutableListOf<resultChartData>()
                for (snapshot in dataSnapshot.children) {
                    val chartData = snapshot.getValue(resultChartData::class.java)
                    chartData?.let {
                        chartDataList.add(it)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }

        recycler_result.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        resultAdapter = ViewAdapter(emptyList()) // 초기에는 빈 데이터로 설정
        recycler_result.adapter = resultAdapter

        recycler_chart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        chartViewAdapter = chartViewAdapter(this, emptyList()) // 초기에는 빈 데이터로 설정
        recycler_chart.adapter = chartViewAdapter

        // Runnable 초기화 및 핸들러 시작
        runnable = this
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun run() {
        // 반복 실행할 코드 추가
        // 예: 일정 시간 간격으로 데이터베이스 업데이트
        handler.postDelayed(runnable, 1000) // 1초마다 실행
    }

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

    private fun saveTextToDatabase1(text: String, context: Context) {
        saveTextToDatabase(dataMotor1, text, context)
    }

    private fun saveTextToDatabase2(text: String, context: Context) {
        saveTextToDatabase(dataMotor2, text, context)
    }
}



