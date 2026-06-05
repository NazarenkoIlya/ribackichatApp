package com.example.rybackiapp.utils.core

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageSender @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun scheduleMessage(chatId: String, text: String) {
        val workRequest = OneTimeWorkRequestBuilder<SendMessageWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    "CHAT_ID" to chatId,
                    "TEXT" to text
                )
            )
            .build()

        workManager.enqueue(workRequest)
    }
}