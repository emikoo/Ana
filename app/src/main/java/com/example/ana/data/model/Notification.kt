package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class Notification(
    val title: String,
    val subtitle: String,
    val picture: String
) {
    companion object {
        fun DocumentSnapshot.toNotification(): Notification? {
            return try {
                val title = getString("title").toString()
                val subtitle = getString("subtitle").toString()
                val picture = getString("picture").toString()
                Notification(title, subtitle, picture)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting Notification", e)
                FirebaseCrashlytics.getInstance().log("Error converting Notification")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "Notification"
    }
}
