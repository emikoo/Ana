package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.Child
import com.example.ana.data.model.Child.Companion.toChild
import com.example.ana.data.model.formatMessageTime
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseHomeService {
    private const val TAG = "FirebaseHomeService"
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    suspend fun getChildren(userId: String): List<Child> {
        return try {
            val array = db.collection("users")
                .document(userId)
                .collection("children")
            array.get().await()
                .documents.mapNotNull { it.toChild() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting children" , e)
            FirebaseCrashlytics.getInstance().log("Error getting children")
            FirebaseCrashlytics.getInstance().setCustomKey("uid" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    fun addChild(userId: String, child: Child) {
        try {
            db.collection("users")
                .document(userId)
                .collection("children").document(child.name).set(child)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding child" , e)
            FirebaseCrashlytics.getInstance().log("Error adding child")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}