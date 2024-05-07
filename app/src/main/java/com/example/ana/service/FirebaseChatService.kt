package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.ChatMessage
import com.example.ana.data.model.ChatMessage.Companion.toMessage
import com.example.ana.data.model.formatMessageTime
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseMessagesService {
    private const val TAG = "FirebaseChatService"
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    suspend fun getMessages(userId: String): List<ChatMessage> {
        return try {
            val array = db.collection("users")
                .document(userId)
                .collection("messages").orderBy("timestampFull")
            array.get().await()
                .documents.mapNotNull { it.toMessage() }
        } catch (e: Exception) {
            Log.e(TAG , "Error getting messages" , e)
            FirebaseCrashlytics.getInstance().log("Error getting messages")
            FirebaseCrashlytics.getInstance().setCustomKey("senderId" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    fun sendMessage(userId: String, message: String) {
        try {
            val messageMap = hashMapOf(
                "prompt" to message,
                "senderId" to userId,
                "timestampFull" to formatMessageTime(Timestamp.now(), true),
                "timestampShort" to formatMessageTime(Timestamp.now(), false)
            )
            db.collection("users")
                .document(userId)
                .collection("messages").add(messageMap)
        } catch (e: Exception) {
            Log.e(TAG , "Error sending message" , e)
            FirebaseCrashlytics.getInstance().log("Error sending message")
            FirebaseCrashlytics.getInstance().setCustomKey("user id" , userId)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}