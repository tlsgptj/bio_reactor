package com.example.bioreactor
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bioreactor.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PREFIX = "data_"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_VALUE = "value"
    }

    override fun onCreate(db: SQLiteDatabase) {
        for (tempId in 1..12) {
            createTable(db, tempId)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        for (tempId in 1..12) {
            db.execSQL("DROP TABLE IF EXISTS ${TABLE_PREFIX}${tempId}")
        }
        onCreate(db)
    }

    private fun createTable(db: SQLiteDatabase, tempId: Int) {
        val createTable = "CREATE TABLE ${TABLE_PREFIX}${tempId} (" +
                "$COLUMN_TIME INTEGER, " +
                "$COLUMN_VALUE REAL)"
        db.execSQL(createTable)
    }

    fun insertData(data: Data, tempId: Int) {
        val db = writableDatabase
        val tableName = "${TABLE_PREFIX}${tempId}"
        val values = ContentValues().apply {
            put(COLUMN_TIME, data.time)
            put(COLUMN_VALUE, data.value)
        }
        db.insert(tableName, null, values)
        db.close()
    }


    fun getDataByAllTempIds(): Map<Int, List<Data>> {
        val db = readableDatabase
        val allData = mutableMapOf<Int, List<Data>>()

        // 1부터 12까지의 tempId를 처리
        for (tempId in 1..12) {
            val tableName = "${TABLE_PREFIX}${tempId}"
            val cursor = db.query(
                tableName,
                arrayOf(COLUMN_TIME, COLUMN_VALUE),
                null,
                null,
                null,
                null,
                COLUMN_TIME
            )

            val dataList = mutableListOf<Data>()
            with(cursor) {
                while (moveToNext()) {
                    val time = getLong(getColumnIndexOrThrow(COLUMN_TIME))
                    val value = getDouble(getColumnIndexOrThrow(COLUMN_VALUE))
                    dataList.add(Data(time, value, tempId))
                }
                close()
            }

            // tempId에 해당하는 데이터를 Map에 추가
            allData[tempId] = dataList
        }

        db.close()
        return allData
    }

}




