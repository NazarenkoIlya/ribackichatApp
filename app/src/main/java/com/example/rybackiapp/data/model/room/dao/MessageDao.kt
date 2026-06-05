package com.example.rybackiapp.data.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rybackiapp.data.model.room.dbo.MessagesDbo

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessagesDbo)

    @Query("SELECT * FROM messages WHERE chatId = :id")
    suspend fun getMessage(id: String): MessagesDbo?

    @Query("DELETE FROM messages WHERE chatId = :id")
    suspend fun deleteMessageById(id: String)
}