package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.WishCard
import com.example.ana.data.model.WishCard.Companion.toWishCard
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseWishCardService {
    private const val TAG = "FirebaseWishCardService"

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    suspend fun getSectionItems(uid: String, sectionName: String): List<WishCard> {
        return try {
            val array = db.collection("users").document(uid)
                .collection("wish_card").document(sectionName)
                .collection("images")
            array.get().await()
                .documents.mapNotNull { it.toWishCard() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting WishCard", e)
            FirebaseCrashlytics.getInstance().log("Error getting WishCard")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getAlbumPictures(): List<WishCard> {
        return try {
            val array = db.collection("gallery").document("finance").collection("images")
            array.get().await()
                .documents.mapNotNull { it.toWishCard() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting WishCard", e)
            FirebaseCrashlytics.getInstance().log("Error getting WishCard")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }


    fun uploadPicture(uid: String, sectionName: String, imageId: String, image: String) {
        try {
            db.collection("users").document(uid)
                .collection("wish_card").document(sectionName)
                .collection("images").document(imageId).update("image", image)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating pic", e)
            FirebaseCrashlytics.getInstance().log("Error updating pic")
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}