package com.wein_business.services

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wein_business.ui.activity.MainActivity

class FirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "Firebase Received  "

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, message.toString())
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("title", message.notification?.title)
        intent.putExtra("body", message.notification?.body)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Firebase Token: $token")
        subscribeToFirebaseTopic()
    }

    private fun subscribeToFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("GeneralAds")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to GeneralAds"
                if (!task.isSuccessful) {
                    msg = "Subscribed Failed ****** to GeneralAds "
                }
                Log.d("Firebase  ", msg)
            }
    }
}