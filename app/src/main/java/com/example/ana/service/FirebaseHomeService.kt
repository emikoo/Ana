package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.Child
import com.example.ana.data.model.Child.Companion.toChild
import com.example.ana.data.model.User
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

    suspend fun getUserName(docId: String): String? {
        return try {
            val name = db.collection("users").document(docId).get().await().get("name").toString()
            name
        } catch (e: Exception) {
            Log.e(TAG, "Error getting name", e)
            FirebaseCrashlytics.getInstance().log("Error getting name")
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getProfilePhoto(docId: String): String? {
        return try {
            val name = db.collection("users").document(docId).get().await().get("photo").toString()
            name
        } catch (e: Exception) {
            Log.e(TAG, "Error getting name", e)
            FirebaseCrashlytics.getInstance().log("Error getting name")
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    fun updateUser(userId: String, user: User) {
        try {
            db.collection("users")
                .document(userId).set(user)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user" , e)
            FirebaseCrashlytics.getInstance().log("Error updating user")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}