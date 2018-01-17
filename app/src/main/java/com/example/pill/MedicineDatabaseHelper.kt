package com.example.pill

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor


class MedicineDatabaseHelper(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {

    companion object {
        private val TABLE_NAME = "tasks_table"
        private val COL1 = "ID"
        private val COL2 = "name"
        private val COL3 = "info"
        private val COL4 = "instruction"
        private val COL5 = "photoLink"
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
                COL5 + " TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addData(name: String, info: String, inst: String, photoLink: String): Boolean {
        val db = this.writableDatabase
        val medicineValue = ContentValues()
        medicineValue.put(COL2, name)
        medicineValue.put(COL3, info)
        medicineValue.put(COL4, inst)
        medicineValue.put(COL5, photoLink)
        val newRowId = db.insert(TABLE_NAME, null, medicineValue)
        return newRowId.compareTo(-1) != 0
    }

}