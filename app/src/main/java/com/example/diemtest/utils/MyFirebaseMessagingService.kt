package com.example.diemtest.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.diemtest.MainActivity
import com.example.diemtest.R
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    private val channelId = "com.example.diemtest"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val i = Intent(this, MainActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val manager = getSystemService(NotificationManager::class.java)
        createNotificationChannel(manager as NotificationManager)

        val inte = PendingIntent.getActivities(this, 0,
            arrayOf(i), PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentIntent(inte)
            .setAutoCancel(true)
            .build()

        manager.notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        val channel = NotificationChannel(channelId, "channelName",
            NotificationManager.IMPORTANCE_HIGH)
        channel.description = "New Chat"
        channel.enableLights(true)
        manager.createNotificationChannel(channel)
    }
}