package com.example.bioreactor

import android.content.Context
import android.util.Log
import com.google.firebase.database.*

class resultDB(private val context: Context) {

    fun getAllSensorData(listener: (List<SensorData>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("https://bio-reacter-default-rtdb.firebaseio.com/") // 실제 데이터베이스 경로를 지정해야 합니다

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<SensorData>()

                for (childSnapshot in snapshot.children) {
                    val temp1 = childSnapshot.child("temp1").getValue(Float::class.java) ?: 0.0f
                    val temp2 = childSnapshot.child("temp2").getValue(Float::class.java) ?: 0.0f
                    val temp3 = childSnapshot.child("temp3").getValue(Float::class.java) ?: 0.0f
                    val temp4 = childSnapshot.child("temp4").getValue(Float::class.java) ?: 0.0f
                    val temp5 = childSnapshot.child("temp5").getValue(Float::class.java) ?: 0.0f
                    val temp6 = childSnapshot.child("temp6").getValue(Float::class.java) ?: 0.0f
                    val temp7 = childSnapshot.child("temp7").getValue(Float::class.java) ?: 0.0f
                    val temp8 = childSnapshot.child("temp8").getValue(Float::class.java) ?: 0.0f
                    val temp9 = childSnapshot.child("temp9").getValue(Float::class.java) ?: 0.0f
                    val temp10 = childSnapshot.child("temp10").getValue(Float::class.java) ?: 0.0f
                    val temp11 = childSnapshot.child("temp11").getValue(Float::class.java) ?: 0.0f
                    val temp12 = childSnapshot.child("temp12").getValue(Float::class.java) ?: 0.0f
                    val ph = childSnapshot.child("ph").getValue(Float::class.java) ?: 0.0f
                    val motorSpeed = childSnapshot.child("motor").getValue(Float::class.java) ?: 0
                    val motorSpeed1 = childSnapshot.child("motor1").getValue(Float::class.java) ?: 0

                    val sensorData = SensorData(
                        temp1,
                        temp2,
                        temp3,
                        temp4,
                        temp5,
                        temp6,
                        temp7,
                        temp8,
                        temp9,
                        temp10,
                        temp11,
                        temp12,
                        ph,
                        motorSpeed,
                        motorSpeed1
                    )
                    dataList.add(sensorData)
                }
                listener(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "error")
                listener(emptyList())
            }
        })
    }
}
