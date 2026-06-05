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
    suspend operator fun invoke(uid: String, title: String, text: String): Result<Unit> {

        return try {
            val token = notificationRepository.getReceiverFcmToken(uid)
            pushNotificationRepository.sendPush(
                receiverFcmToken = token,
                title = title,
                text = text
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                500 -> "Ошибка сервера" //resourcesManager.getString(R.string.server_error, e.code().toString())
                else -> "Неизвестная ошибка"// resourcesManager.getString(R.string.unknown_error)
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}