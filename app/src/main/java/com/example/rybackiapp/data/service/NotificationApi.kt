package com.example.rybackiapp.data.service



import retrofit2.http.Body
import retrofit2.http.POST


interface NotificationApi {
    @POST ("send-notification")
    suspend fun sendNotification(@Body request: NotificationRequest)
}

data class NotificationRequest(
    val token: String,
    val title: String,
    val body: String,
    val chatId: String
)