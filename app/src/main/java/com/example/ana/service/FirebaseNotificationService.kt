package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.Notification
import com.example.ana.data.model.Notification.Companion.toNotification
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseNotificationService {
    private const val TAG = "FirebaseNotificationService"

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    suspend fun getNotifications(): List<Notification> {
        return try {
            val child = db.collection("notifications").get().await().documents.mapNotNull { it.toNotification() }
            child
        } catch (e: Exception) {
            Log.e(TAG, "Error getting getNotifications" , e)
            FirebaseCrashlytics.getInstance().log("Error getting getNotifications")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

}