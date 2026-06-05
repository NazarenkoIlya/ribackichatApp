package com.example.rybackiapp.domain.repository

interface NotificationRepository {
    suspend fun saveFcmToken(uid: String, token: String)
    suspend fun getReceiverFcmToken(uid: String): String
    suspend fun removeFcmToken(uid: String)
}