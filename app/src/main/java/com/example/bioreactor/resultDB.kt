package com.example.bioreactor

import android.content.Context
import android.util.Log
import com.google.firebase.database.*

class resultDB(private val context: Context) {

    fun getAllSensorData(listener: (List<SensorData>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("https://bioreactor-bb6b7-default-rtdb.asia-southeast1.firebasedatabase.app/") // 실제 데이터베이스 경로를 지정해야 합니다

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<SensorData>()

                for (childSnapshot in snapshot.children) {
                    val temp1 = childSnapshot.child("temp/temp1").getValue(Float::class.java) ?: 0.0f
                    val temp2 = childSnapshot.child("temp/temp2").getValue(Float::class.java) ?: 0.0f
                    val temp3 = childSnapshot.child("temp/temp3").getValue(Float::class.java) ?: 0.0f
                    val temp4 = childSnapshot.child("temp/temp4").getValue(Float::class.java) ?: 0.0f
                    val temp5 = childSnapshot.child("temp/temp5").getValue(Float::class.java) ?: 0.0f
                    val temp6 = childSnapshot.child("temp/temp6").getValue(Float::class.java) ?: 0.0f
                    val temp7 = childSnapshot.child("temp/temp7").getValue(Float::class.java) ?: 0.0f
                    val temp8 = childSnapshot.child("temp/temp8").getValue(Float::class.java) ?: 0.0f
                    val temp9 = childSnapshot.child("temp/temp9").getValue(Float::class.java) ?: 0.0f
                    val temp10 = childSnapshot.child("temp/temp10").getValue(Float::class.java) ?: 0.0f
                    val temp11 = childSnapshot.child("temp/temp11").getValue(Float::class.java) ?: 0.0f
                    val temp12 = childSnapshot.child("temp/temp12").getValue(Float::class.java) ?: 0.0f
                    val heattemp = childSnapshot.child("temp/heatTemp").getValue(Float::class.java) ?: 0.0f
                    val heatPow = childSnapshot.child("temp/heatPow").getValue(Float::class.java) ?: 0.0f
                    val ph = childSnapshot.child("PH").getValue(Float::class.java) ?: 0.0f

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
                        heattemp,
                        heatPow,
                        ph
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
