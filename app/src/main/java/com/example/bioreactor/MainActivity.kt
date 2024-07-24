package com.example.bioreactor

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
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext as withContext1

class MainActivity : AppCompatActivity(), Runnable {
    private lateinit var UV_button: Button
    private lateinit var Video: VideoView
    private lateinit var time: TextView
    private lateinit var recyclerResult: RecyclerView
    private lateinit var motorText1: EditText
    private lateinit var motorText2: EditText
    private lateinit var recyclerChart: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var dataUV: DatabaseReference
    private lateinit var dataTemp: DatabaseReference
    private lateinit var dataMotor1: DatabaseReference
    private lateinit var dataMotor2: DatabaseReference
    private lateinit var databasePH: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var resultChartData: CombinedChart

    private val handler = Handler()
    private lateinit var resultAdapter: ViewAdapter
    private lateinit var chartViewAdapter: chartViewAdapter
    private lateinit var runnable: Runnable
    private val dbHelper = MyDatabaseHelper(this)
    private val dataSets = mutableListOf<List<Entry>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        // 뷰 초기화
        UV_button = findViewById(R.id.UV)
        Video = findViewById(R.id.Video)
        time = findViewById(R.id.time)
        motorText1 = findViewById(R.id.motor_text1)
        motorText2 = findViewById(R.id.motor_text2)
        recyclerResult = findViewById(R.id.recycler_result)
        recyclerChart = findViewById(R.id.recycler_chart)

        // Firebase 데이터베이스 초기화
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        dataUV = database.getReference("UV")
        dataTemp = database.getReference("temp")
        dataMotor1 = database.getReference("motor")
        dataMotor2 = database.getReference("motor1")
        databasePH = database.getReference("PH")
        storage = FirebaseStorage.getInstance()

        // RecyclerView 설정
        recyclerResult.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        resultAdapter = ViewAdapter(emptyList())
        recyclerResult.adapter = resultAdapter

        recyclerChart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        chartViewAdapter = chartViewAdapter(this, emptyList())
        recyclerChart.adapter = chartViewAdapter

        // UV 버튼 클릭 리스너 설정
        UV_button.setOnClickListener {
            toggleUVStatus()
        }

        // 데이터 리스너 추가
        dataTemp.addValueEventListener(chartDataListener)
        dataUV.addValueEventListener(dataChangeListener)

        // Runnable 초기화 및 시작
        runnable = this
        handler.post(runnable)

        // 데이터를 불러와 차트에 표시
        fetchDataFromFirebase()
    }

    // UV 상태 토글 메서드
    private fun toggleUVStatus() {
        dataUV.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isUVOn = dataSnapshot.getValue(Boolean::class.java) ?: true
                dataUV.setValue(!isUVOn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("failed", "UV 상태 업데이트 실패")
            }
        })
    }

    // 차트 데이터 리스너
    private val chartDataListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val chartDataList = mutableListOf<Data>()
            for (snapshot in dataSnapshot.children) {
                val chartData = snapshot.getValue(Data::class.java)
                chartData?.let { chartDataList.add(it) }
            }
            loadDataAndDisplayCharts()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("MainActivity", "값을 읽어오는데 실패했습니다.", databaseError.toException())
        }
    }

    // 데이터 변화 리스너
    private val dataChangeListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val isActive = when (val value = dataSnapshot.child("true").value) {
                is Boolean -> value
                is Long -> value != 0L
                is Int -> value != 0
                else -> false
            }

            val count = dataSnapshot.child("count").getValue(Long::class.java) ?: 0L

            updateUI(isActive, count)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("MainActivity", "데이터를 읽어오는데 실패했습니다.", databaseError.toException())
        }
    }

    // UI 업데이트 메서드
    private fun updateUI(isActive: Boolean, count: Long) {
        // UI 업데이트 로직을 여기에 추가합니다.
        Log.d("MainActivity", "isActive: $isActive, count: $count")
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun run() {
        // 반복 실행할 코드
        handler.postDelayed(runnable, 10000)
    // 1초마다 실행
    }

    private fun fetchDataFromFirebase() {
        val tempIds = 1..12 // 1부터 12까지의 tempId 목록

        CoroutineScope(Dispatchers.IO).launch {
            var dataCount = 0
            val totalTempIds = tempIds.count()

            for (tempId in tempIds) {
                val tempRef = database.getReference("temp$tempId")
                tempRef.get().addOnSuccessListener { snapshot ->
                    val dataList = mutableListOf<Data>()

                    for (child in snapshot.children) {
                        val time = child.child("time").getValue(Long::class.java) ?: continue
                        val value = child.child("value").getValue(Double::class.java) ?: continue
                        val dataEntry = Data(time, value, tempId)
                        dataList.add(dataEntry)
                    }

                    if (dataList.isNotEmpty()) {
                        for (data in dataList) {
                            dbHelper.insertData(data, tempId)
                        }
                    }

                    dataCount++

                    if (dataCount == totalTempIds) {
                        // Update the UI on the main thread
                        launch(Dispatchers.Main) {
                            loadDataAndDisplayCharts()
                        }
                    }
                }.addOnFailureListener { error ->
                    Log.w("MainActivity", "데이터를 읽어오는데 실패했습니다.", error)
                }
            }
        }
    }






    private fun loadDataAndDisplayCharts() {
        CoroutineScope(Dispatchers.IO).launch {
            dataSets.clear()

            val allData = dbHelper.getDataByAllTempIds()

            for (tempId in 1..12) {
                val data = allData[tempId] ?: emptyList()
                val entries = data.map { Entry(it.time.toFloat(), it.value.toFloat()) }
                dataSets.add(entries)
            }

            withContext1(Dispatchers.Main) {
                chartViewAdapter.updateData(dataSets)
            }
        }
    }



    private fun saveTextToDatabase(reference: DatabaseReference, text: String, context: Context) {
        val key = reference.push().key
        if (key != null) {
            reference.child(key).setValue(text)
                .addOnCompleteListener { task ->
                    val message = if (task.isSuccessful) {
                        "데이터가 성공적으로 저장되었습니다."
                    } else {
                        "데이터 저장에 실패하였습니다."
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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







