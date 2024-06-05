package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class SoundSession(
    val id: Int,
    val name: String,
    val duration: String,
    val tried: Boolean,
    val preview: String,
    val url: String
) {
    companion object {
        fun DocumentSnapshot.toSoundSession(): SoundSession? {
            return try {
                val id = get("id").toString().toInt()
                val name = getString("name").toString()
                val duration = getString("duration")!!
                val tried = getBoolean("tried")!!
                val preview = getString("preview")!!
                val url = getString("url")!!
                SoundSession(id, name, duration, tried, preview, url)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting sound session", e)
                FirebaseCrashlytics.getInstance().log("Error converting sound session")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "SoundSession"
    }
}
