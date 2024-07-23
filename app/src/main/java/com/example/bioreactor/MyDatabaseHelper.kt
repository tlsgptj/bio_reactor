package com.example.bioreactor
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bioreactor.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "data"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_VALUE = "value"
        private const val COLUMN_TEMP_ID = "temp_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_TIME INTEGER, " +
                "$COLUMN_VALUE REAL, " +
                "$COLUMN_TEMP_ID INTEGER)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(data: data, tempId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TIME, data.time)
            put(COLUMN_VALUE, data.value)
            put(COLUMN_TEMP_ID, tempId)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun getDataGroupedByTempId(tempId: Int): List<data> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TIME, COLUMN_VALUE),
            "$COLUMN_TEMP_ID = ?",
            arrayOf(tempId.toString()),
            null,
            null,
            COLUMN_TIME
        )

        val dataList = mutableListOf<data>()
        with(cursor) {
            while (moveToNext()) {
                val time = getLong(getColumnIndexOrThrow(COLUMN_TIME))
                val value = getDouble(getColumnIndexOrThrow(COLUMN_VALUE))
                dataList.add(data(time, value))
            }
            close()
        }
        return dataList
    }
}



