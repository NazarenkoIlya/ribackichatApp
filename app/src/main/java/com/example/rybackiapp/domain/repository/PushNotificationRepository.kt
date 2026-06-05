package com.example.rybackiapp.domain.repository

interface PushNotificationRepository {
    suspend fun sendPush(
        receiverFcmToken: String,
        title: String,
        text: String
    )
}