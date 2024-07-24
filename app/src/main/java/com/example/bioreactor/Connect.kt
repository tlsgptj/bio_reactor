package com.example.bioreactor

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Connect {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var myDatabaseHelper: MyDatabaseHelper

    fun setupListener(context: Context) {
        databaseReference = FirebaseDatabase.getInstance().getReference("temp")
        myDatabaseHelper = MyDatabaseHelper(context)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(Data::class.java)
                    if (data != null) {
                        for (tempId in 1..12) {
                            val tempValue = dataSnapshot.child("temp$tempId").getValue(Int::class.java) ?: continue
                            myDatabaseHelper.insertData(data, tempId)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Database error: $error")
            }
        })
    }
}
