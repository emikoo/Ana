package com.example.ana.presentation.ui.fragments.main.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.data.model.ChatMessage
import com.example.ana.data.model.Message
import com.example.ana.data.model.MessageType
import com.example.ana.data.model.formatMessageTime
import com.example.ana.databinding.FragmentChatBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.teenteen.teencash.presentation.base.BaseFragment

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private lateinit var adapter: MessageAdapter
    private lateinit var chatMessages: MutableList<ChatMessage>
    private lateinit var viewModel: ChatViewModel
    override fun attachBinding(
        list: MutableList<FragmentChatBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentChatBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun setupViews() {
        chatMessages = mutableListOf()
        adapter = MessageAdapter(chatMessages)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
        binding.send.setOnClickListener {
            val messageText = binding.etMessage.text.trim().toString()
            if (messageText.isNotEmpty()) {
                sendMessageToFirestore(messageText)
                binding.etMessage.setText("")
            }
//            setupMessageListener()
        }
    }

    fun sendMessageToFirestore(message: String) {
        val messageMap = hashMapOf(
            "prompt" to message,
            "senderId" to prefs.getCurrentUserId(),
            "timestampFull" to formatMessageTime(Timestamp.now(), true),
            "timestampShort" to formatMessageTime(Timestamp.now(), false)
        )
        db.collection("users").document(prefs.getCurrentUserId()).collection("messages").add(messageMap)
    }

    override fun subscribeToLiveData() {
        setupMessageListener()
    }
    fun setupMessageListener() {
        db.collection("users").document(prefs.getCurrentUserId()).collection("messages").orderBy("timestampFull")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ChatFragment", "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val message = dc.document.toObject(Message::class.java)
                        val user = ChatMessage(parentMessageId = message.parentMessageId, senderId = message.senderId,
                        response = "", prompt = message.prompt, timestampFull = message.timestampFull, timestampShort = message.timestampShort, type = MessageType.USER)
                        val bot = ChatMessage(parentMessageId = message.parentMessageId, senderId = message.senderId,
                            response = message.response, prompt = "", timestampFull = message.timestampFull, timestampShort = message.timestampShort, type = MessageType.BOT)
                        updateUIWithNewMessage(user)
                        updateUIWithNewMessage(bot)
                    }
                }
            }
    }

    fun updateUIWithNewMessage(message: ChatMessage) {
        chatMessages.add(message)
        adapter.notifyItemInserted(chatMessages.size - 1)
        binding.rvChat.scrollToPosition(chatMessages.size - 1)
    }
}