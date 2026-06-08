package com.example.rybackiapp.domain.usecase

import android.util.Log
import com.example.rybackiapp.domain.repository.NotificationRepository
import com.example.rybackiapp.domain.repository.PushNotificationRepository
import retrofit2.HttpException
import javax.inject.Inject

class PushNotificationUseCase @Inject constructor(
    private val pushNotificationRepository: PushNotificationRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        uid: String,
        title: String,
        text: String,
        chatId: String
    ): Result<Unit> {

        return try {
            val token = notificationRepository.getReceiverFcmToken(uid)
            pushNotificationRepository.sendPush(
                receiverFcmToken = token,
                title = title,
                text = text,
                chatId = chatId
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                500 -> "Ошибка сервера"
                else -> "Неизвестная ошибка"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}