package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class Advice(
    val title: String,
    val article: String,
    val picture: String,
    val category: String,
    val language: String,
    val ageWeeks: Int,
    val created: String
) {
    companion object {
        fun DocumentSnapshot.toAdvice(): Advice? {
            return try {
                val title = getString("title").toString()
                val article = getString("article").toString()
                val picture = getString("picture").toString()
                val category = getString("category").toString()
                val language = getString("language").toString()
                val ageWeeks = get("ageWeeks").toString().toInt()
                val created = getString("created").toString()
                Advice(title, article, picture, category, language, ageWeeks, created)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting Advice", e)
                FirebaseCrashlytics.getInstance().log("Error converting Advice")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "Meditation"
    }
}
