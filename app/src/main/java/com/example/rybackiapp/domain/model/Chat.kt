package com.example.rybackiapp.domain.model

import android.icu.text.CaseMap
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


sealed interface Chat {
    val chatId: String
}


data class PrivateChat(
    override val chatId: String,
    val participants: List<String>,
    val lastSenderId: String? = null,
    val lastMessage: String? = null,
    val lastTimestamp: Long? = null
) : Chat


data class MessageDraft(
    val chatId: String,
    val text: String
)


data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val isEdit: Boolean? = null
)
