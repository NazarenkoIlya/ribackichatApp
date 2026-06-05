package com.example.rybackiapp.utils.core


import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rybackiapp.domain.usecase.SaveFcmTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var saveFcmTokenUseCase: SaveFcmTokenUseCase


    override fun onCreate() {
        super.onCreate()
        Log.d("IIIII", "🔥🔥🔥 MyFirebaseMessagingService СОЗДАН 🔥🔥🔥")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("IIIII", "💀💀💀 MyFirebaseMessagingService УНИЧТОЖЕН 💀💀💀")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("DDDDDD", "Refreshed token: $token")

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return


        CoroutineScope(Dispatchers.IO).launch {
            saveFcmTokenUseCase.invoke(uid, token)
        }

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(uid)
            .child("fcmToken")
            .setValue(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: "New message"
        val body = message.notification?.body ?: ""

        Log.d("IIIII", "onMessageReceived: ${title} --- $body")

        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "chat_channel"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val existingChannel = notificationManager.getNotificationChannel(channelId)
            if (existingChannel == null) {
                val channel = NotificationChannel(
                    channelId,
                    "Chat Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Уведомления о новых сообщениях"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)

                // Добавим лог для проверки создания канала
                Log.d("IIIII", "Канал создан: ${channel.id}")
            } else {
                Log.d("IIIII", "Канал уже существует, importance: ${existingChannel.importance}")
            }
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification_overlay)
//            .setPriority(NotificationCompat.PRIORITY_HIGH) // Добавь priority
//            .setDefaults(NotificationCompat.DEFAULT_ALL) // Звук, вибрация, свет
            .setAutoCancel(true)
            .build()

        Log.d("IIIII", "showNotification: $notification")
        notificationManager.notify(Random.nextInt(), notification)

    }
}