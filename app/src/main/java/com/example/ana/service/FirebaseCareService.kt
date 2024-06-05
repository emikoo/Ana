package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.Meditation
import com.example.ana.data.model.Meditation.Companion.toMeditation
import com.example.ana.data.model.Podcast
import com.example.ana.data.model.Podcast.Companion.toPodcast
import com.example.ana.data.model.SoundSession
import com.example.ana.data.model.SoundSession.Companion.toSoundSession
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseCareService {
    private const val TAG = "FirebaseCareService"

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    suspend fun getMeditation(): List<Meditation> {
        return try {
            val array = db.collection("meditation")
            array.get().await()
                .documents.mapNotNull { it.toMeditation() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting meditation", e)
            FirebaseCrashlytics.getInstance().log("Error getting meditation")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getPodcast(): List<Podcast> {
        return try {
            val array = db.collection("podcasts").orderBy("id")
            array.get().await()
                .documents.mapNotNull { it.toPodcast() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting podcasts", e)
            FirebaseCrashlytics.getInstance().log("Error getting podcasts")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getSoundSession(): List<SoundSession> {
        return try {
            val array = db.collection("meditation").document("first").collection("sounds")
            array.get().await()
                .documents.mapNotNull { it.toSoundSession() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting soundSession", e)
            FirebaseCrashlytics.getInstance().log("Error getting soundSession")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getPodcastExceptItem(itemId: Int): List<Podcast> {
        return try {
            val array = db.collection("podcasts").orderBy("id").whereNotEqualTo("id", itemId)
            array.get().await()
                .documents.mapNotNull { it.toPodcast() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting podcasts", e)
            FirebaseCrashlytics.getInstance().log("Error getting podcasts")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getItemPodcast(itemId: Int): Podcast? {
        return try {
            val podcast =
                db.collection("podcasts").orderBy("id").get().await().documents[itemId].toPodcast()
            podcast
        } catch (e: Exception) {
            Log.e(TAG, "Error getting podcast", e)
            FirebaseCrashlytics.getInstance().log("Error getting podcast")
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getItemMeditation(itemId: Int): Meditation? {
        return try {
            val podcast = db.collection("meditation").get().await().documents[itemId].toMeditation()
            podcast
        } catch (e: Exception) {
            Log.e(TAG, "Error getting meditation", e)
            FirebaseCrashlytics.getInstance().log("Error getting meditation")
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    fun updateViews(podcastId: Int, views: Int) {
        try {
            db.collection("podcasts").document("nuradam$podcastId").update("views", views)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating views", e)
            FirebaseCrashlytics.getInstance().log("Error updating views")
            FirebaseCrashlytics.getInstance().setCustomKey("user id", podcastId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateSessionCount(count: Int) {
        try {
            db.collection("meditation").document("first").update("count", count)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating count", e)
            FirebaseCrashlytics.getInstance().log("Error updating count")
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun updateSoundTried(id: Int) {
        try {
            var docName = "first"
            when (id) {
                1 -> docName = "first"
                2 -> docName = "second"
                3 -> docName = "third"
                4 -> docName = "forth"
                5 -> docName = "fifth"
            }
            db.collection("meditation").document("first").collection("sounds")
                .document(docName).update("tried", true)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating count", e)
            FirebaseCrashlytics.getInstance().log("Error updating count")
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}