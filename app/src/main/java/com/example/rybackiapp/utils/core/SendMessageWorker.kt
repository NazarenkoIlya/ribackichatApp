package com.example.rybackiapp.utils.core

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.rybackiapp.domain.usecase.SendMessageInPrivateChatUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SendMessageWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val sendMessageInPrivateChatUseCase: SendMessageInPrivateChatUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val chatId = inputData.getString("CHAT_ID") ?: return@withContext Result.failure()
        val text = inputData.getString("TEXT") ?: return@withContext Result.failure()

        return@withContext try {
            sendMessageInPrivateChatUseCase(chatId = chatId, text = text)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}