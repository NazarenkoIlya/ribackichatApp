package com.example.rybackiapp.data.model.room

import com.example.rybackiapp.data.model.room.dbo.MessagesDbo
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rybackiapp.data.model.room.dao.MessageDao

@Database(entities = [MessagesDbo::class], version = 1, exportSchema = false)
abstract class MessagesDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}


