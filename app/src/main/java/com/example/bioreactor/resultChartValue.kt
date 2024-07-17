package com.example.bioreactor

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object resultChartValue {
    const val COLUMN_NAME = "name"
}

class Database(context: Context) : SQLiteOpenHelper(context, "resultValue.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${resultChartValue} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${resultChartValue.COLUMN_NAME} TEXT," +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData() {
        val db = writableDatabase
        val values = ContentValues().apply {
                put(resultChartValue.COLUMN_NAME, "temp1")
                put(resultChartValue.COLUMN_NAME, "temp2")
                put(resultChartValue.COLUMN_NAME, "temp3")
                put(resultChartValue.COLUMN_NAME, "temp4")
                put(resultChartValue.COLUMN_NAME, "temp5")
                put(resultChartValue.COLUMN_NAME, "temp6")
                put(resultChartValue.COLUMN_NAME, "temp7")
                put(resultChartValue.COLUMN_NAME, "temp8")
                put(resultChartValue.COLUMN_NAME, "temp9")
                put(resultChartValue.COLUMN_NAME, "temp10")
                put(resultChartValue.COLUMN_NAME, "PH")
                put(resultChartValue.COLUMN_NAME, "motorSpeed")
            }
            db.insert(resultChartValue.COLUMN_NAME, null, values)


        db.close()
    }

}