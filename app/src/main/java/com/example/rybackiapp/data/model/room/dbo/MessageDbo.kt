package com.example.rybackiapp.data.model.room.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class MessagesDbo(
    @PrimaryKey(autoGenerate = false)
    val chatId: String,
    val text: String
)

