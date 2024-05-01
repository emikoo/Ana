package com.example.ana.data.model

import com.google.firebase.Timestamp
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
    val status: Status? = null
)

data class ChatMessage(
    val parentMessageId: String = "",
    val prompt: String = "",
    val response: String = "",
    val senderId: String = "",
    val timestampFull: String = formatMessageTime(Timestamp.now(), true),
    val timestampShort: String = formatMessageTime(Timestamp.now(), false),
    val status: Status? = null,
    val type: MessageType)

data class Status(
    val created_at: Timestamp = Timestamp.now(),
    val state: String = "PROGRESS",
    val updated_at: Timestamp = Timestamp.now()
)

enum class MessageType {
    USER, BOT, TYPING
}

fun formatMessageTime(timestamp: Timestamp, full: Boolean): String {
    val millis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val date = Date(millis)
    val pattern = if (full) "dd MMM yyyy HH:mm" else "HH:mm"
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}
