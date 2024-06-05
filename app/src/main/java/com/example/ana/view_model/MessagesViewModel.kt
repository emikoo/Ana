package com.example.ana.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ana.data.model.ChatMessage
import com.example.ana.service.FirebaseMessagesService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MessagesViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages
    private val _lastMessage = MutableLiveData<ChatMessage?>()
    val lastMessage: LiveData<ChatMessage?> get() = _lastMessage

    fun getMessages(uid: String) {
        viewModelScope.launch {
            _messages.value = FirebaseMessagesService.getMessages(uid)
        }
    }

    fun sendMessage(uid: String, message: String) {
        viewModelScope.launch {
            FirebaseMessagesService.sendMessage(uid, message)
        }
    }

    fun monitorLastMessage(userId: String, prompt: String) {
        val db = FirebaseFirestore.getInstance()
        val messagesRef = db.collection("users")
            .document(userId)
            .collection("messages")

        messagesRef
            .whereEqualTo("prompt", prompt)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("MessagesViewModel", "Error listening to changes", e)
                    return@addSnapshotListener
                }

                for (doc in snapshots!!.documents) {
                    val message = doc.toObject(ChatMessage::class.java)
                    if (message?.response?.isNotEmpty() == true) {
                        _lastMessage.postValue(message)
                    }
                }
            }
    }
}