package com.example.pill

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor


class NotificationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {

    companion object {
        private val TABLE_NAME = "notification_table"
        private val COL1 = "ID"
        private val COL2 = "name"
        private val COL3 = "dose"
        private val COL4 = "min"
        private val COL5 = "hour"
        private val COL6 = "isRepeat"
        private val COL7 = "isStored"
        private val COL8 = "beforeEat"
        private val COL9 = "afterEat"

    }

    val data: Cursor
        get() {
            val db = this.writableDatabase
            val query = "SELECT * FROM " + TABLE_NAME
            return db.rawQuery(query, null)
        }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT, " +
                COL8 + " TEXT, " +
                COL9 + " TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addData(name: String,
                dose: String,
                min: Int,
                hour: Int,
                isRepeat: Int,
                isStored: Int,
                beforeEat: Int,
                afterEat: Int): Long {
        val db = this.writableDatabase
        val medicineValue = ContentValues()
        medicineValue.put(COL2, name)
        medicineValue.put(COL3, dose)
        medicineValue.put(COL4, min)
        medicineValue.put(COL5, hour)
        medicineValue.put(COL6, isRepeat)
        medicineValue.put(COL7, isStored)
        medicineValue.put(COL8, beforeEat)
        medicineValue.put(COL9, afterEat)

        return db.insert(TABLE_NAME, null, medicineValue)
    }

    fun updateIsStored(id: Int, newIsStored: Int) {
            val db = this.writableDatabase
            val query = "UPDATE " + TABLE_NAME + " SET " +
                    COL7 + " = '" + newIsStored +
                    "' WHERE " + COL1 + " = '" + id + "'"
            db.execSQL(query)
    }

}