package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

data class Meditation(
    val name: String,
    val preview: String,
    val description: String,
    val count: Int,
    val sessions: Int,
    val locked: Boolean
) {
    companion object {
        fun DocumentSnapshot.toMeditation(): Meditation? {
            return try {
                val name = getString("name") !!
                val preview = getString("preview") !!
                val description = getString("description") !!
                val count = get("count").toString().toInt()
                val sessions = get("sessions").toString().toInt()
                val locked = getBoolean("locked") !!
                Meditation(name,preview, description, count, sessions, locked)
            } catch (e: Exception) {
                Log.e(TAG , "Error converting meditation" , e)
                FirebaseCrashlytics.getInstance().log("Error converting meditation")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }
        private const val TAG = "Meditation"
    }
}
