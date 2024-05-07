package com.example.ana.presentation.ui.fragments.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ana.R
import com.example.ana.data.model.ChatMessage
import com.example.ana.data.model.MessageType

enum class ViewType {
    USER_MESSAGE, BOT_MESSAGE, TYPING_INDICATOR
}

class MessageAdapter(private val messages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].type) {
            MessageType.USER -> ViewType.USER_MESSAGE.ordinal
            MessageType.BOT -> ViewType.BOT_MESSAGE.ordinal
            MessageType.TYPING -> ViewType.TYPING_INDICATOR.ordinal
            else -> {ViewType.USER_MESSAGE.ordinal}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        return UserMessageViewHolder(inflater.inflate(R.layout.message_item_user, parent, false))
        return when(viewType) {
            ViewType.USER_MESSAGE.ordinal -> UserMessageViewHolder(inflater.inflate(R.layout.message_item_user, parent, false))
            ViewType.BOT_MESSAGE.ordinal -> BotMessageViewHolder(inflater.inflate(R.layout.message_item_bot, parent, false))
            ViewType.TYPING_INDICATOR.ordinal -> TypingViewHolder(inflater.inflate(R.layout.message_item_bot, parent, false))
            else -> throw IllegalArgumentException("Unsupported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserMessageViewHolder -> holder.bind(messages[position])
            is BotMessageViewHolder -> holder.bind(messages[position])
            is TypingViewHolder -> holder.bind()
        }
    }

    override fun getItemCount() = messages.size

    fun removeLastItem() {
        if (messages.isNotEmpty()) {
            messages.removeAt(messages.size - 1)
            notifyItemRemoved(messages.size - 1)
        }
    }

    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textMessage)
        private val timeTextView: TextView = itemView.findViewById(R.id.textTime)

        fun bind(message: ChatMessage) {
            messageTextView.text = "   ${message.prompt}"
            timeTextView.text = message.timestampShort
        }
    }

    class BotMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textMessage)
        private val timeTextView: TextView = itemView.findViewById(R.id.textTime)

        fun bind(message: ChatMessage) {
            if (message.response.trim().isBlank()) messageTextView.text = "Печатает..."
            else messageTextView.text = message.response
            timeTextView.text = message.timestampShort
        }
    }

    class TypingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {}
    }
}
