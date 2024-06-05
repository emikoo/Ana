package com.example.ana.data.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val parentMessageId: String = "",
    val prompt: String = "",
    val response: String = "",
    val senderId: String = "",
    val timestampFull: String = formatMessageTime(Timestamp.now(), true),
    val timestampShort: String = formatMessageTime(Timestamp.now(), false),
)

data class ChatMessage(
    val parentMessageId: String = "",
    val prompt: String = "",
    val response: String = "",
    val senderId: String = "",
    val timestampFull: String = formatMessageTime(Timestamp.now(), true),
    val timestampShort: String = formatMessageTime(Timestamp.now(), false),
    val type: MessageType? = null
) {

    companion object {
        fun DocumentSnapshot.toMessage(): ChatMessage? {
            return try {
                val parentMessageId = getString("parentMessageId")!!
                val prompt = getString("prompt")!!
                val response = getString("response")!!
                val senderId = getString("senderId")!!
                val timestampFull = getString("timestampFull")!!
                val timestampShort = getString("timestampShort")!!
                ChatMessage(
                    parentMessageId,
                    prompt,
                    response,
                    senderId,
                    timestampFull,
                    timestampShort
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error converting message", e)
                FirebaseCrashlytics.getInstance().log("Error converting message")
                FirebaseCrashlytics.getInstance().setCustomKey("senderId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "Message"
    }
}

enum class MessageType {
    USER, BOT
}

fun formatMessageTime(timestamp: Timestamp, full: Boolean): String {
    val millis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val date = Date(millis)
    val pattern = if (full) "dd MMM yyyy HH:mm:ss" else "HH:mm"
    val format = SimpleDateFormat(pattern, Locale.ENGLISH)
    return format.format(date)
}