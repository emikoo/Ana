package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

data class Podcast(
    val id: Int,
    val name: String,
    val author: String,
    val preview: String,
    val url: String,
    val views: Int
) {
    companion object {
        fun DocumentSnapshot.toPodcast(): Podcast? {
            return try {
                val id = get("id").toString().toInt() !!
                val name = getString("name") !!
                val author = getString("author") !!
                val preview = getString("preview") !!
                val url = getString("url") !!
                val views = get("views").toString().toInt() !!
                Podcast(id, name, author, preview, url, views)
            } catch (e: Exception) {
                Log.e(TAG , "Error converting podcast" , e)
                FirebaseCrashlytics.getInstance().log("Error converting podcast")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }
        private const val TAG = "Podcast"
    }
}
