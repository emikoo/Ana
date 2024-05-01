package com.example.ana.presentation.ui.fragments.main.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ana.data.model.ChatMessage
import com.example.ana.data.model.MessageType

class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData<MutableList<ChatMessage>>()
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    init {
        _messages.value = mutableListOf()
    }

    fun addMessage(message: ChatMessage) {
        _messages.value?.add(message)
        _messages.value = _messages.value
    }

    fun removeTypingIndicator() {
        val index = _messages.value?.indexOfFirst { it.type == MessageType.TYPING }
        if (index != null && index != -1) {
            _messages.value?.removeAt(index)
            _messages.value = _messages.value
        }
    }
}
