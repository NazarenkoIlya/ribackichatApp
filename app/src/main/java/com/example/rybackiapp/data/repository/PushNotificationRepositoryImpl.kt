package com.example.rybackiapp.data.repository

import android.util.Log
import com.example.rybackiapp.data.service.NotificationApi
import com.example.rybackiapp.data.service.NotificationRequest
import com.example.rybackiapp.domain.repository.PushNotificationRepository
import javax.inject.Inject

class PushNotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi
) : PushNotificationRepository {
    override suspend fun sendPush(
        receiverFcmToken: String,
        title: String,
        text: String,
        chatId: String
    ) {

        api.sendNotification(
            NotificationRequest(
                token = receiverFcmToken,
                title = title,
                body = text,
                chatId = chatId
            )
        )
    }
}