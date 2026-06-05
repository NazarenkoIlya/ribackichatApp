package com.example.rybackiapp.data.repository

import android.util.Log
import androidx.room.Database
import com.example.rybackiapp.domain.repository.NotificationRepository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : NotificationRepository {
    override suspend fun saveFcmToken(uid: String, token: String) {
        database.child("users")
            .child(uid)
            .child("fcmToken")
            .setValue(token)
            .await()
    }

    override suspend fun getReceiverFcmToken(uid: String): String {
        val snapshot = database.child("users").child(uid).child("fcmToken").get().await()
        val token = snapshot.getValue(String::class.java) ?: ""
        return token
    }

    override suspend fun removeFcmToken(uid: String) {

        Log.d("EEEEEE", "removeFcmToken: $uid")
        database.child("users")
            .child(uid)
            .child("fcmToken")
            .removeValue()
            .await()
    }
}