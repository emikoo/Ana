package com.example.ana.presentation.ui.fragments.main.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.data.model.ChatMessage
import com.example.ana.data.model.Message
import com.example.ana.data.model.MessageType
import com.example.ana.databinding.FragmentChatBinding
import com.example.ana.view_model.MessagesViewModel
import com.google.firebase.firestore.DocumentChange
import com.teenteen.teencash.presentation.base.BaseFragment

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private lateinit var adapter: MessageAdapter
    private lateinit var chatMessages: MutableList<ChatMessage>
    private lateinit var viewModel: MessagesViewModel

    override fun attachBinding(
        list: MutableList<FragmentChatBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentChatBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun setupViews() {
        progressDialog.show()
        chatMessages = mutableListOf()
        adapter = MessageAdapter(chatMessages)
        viewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getMessages(prefs.getCurrentUserId())
        binding.send.setOnClickListener {
            val messageText = binding.etMessage.text.trim().toString()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(prefs.getCurrentUserId(), messageText)
                progressDialog.show()
                viewModel.monitorLastMessage(prefs.getCurrentUserId(), messageText)
                binding.etMessage.setText("")
            }
        }
    }

    override fun subscribeToLiveData() {
        viewModel.messages.observe(viewLifecycleOwner) {
            setupMessageListener()
            progressDialog.dismiss()
        }
        viewModel.lastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                val bot = ChatMessage(
                    parentMessageId = message.parentMessageId,
                    senderId = message.senderId,
                    response = message.response,
                    prompt = "",
                    timestampFull = message.timestampFull,
                    timestampShort = message.timestampShort,
                    type = MessageType.BOT
                )
                updateResponse(bot)
            }
        }
    }

    private fun setupMessageListener() {
        db.collection("users").document(prefs.getCurrentUserId()).collection("messages")
            .orderBy("timestampFull")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ChatFragment", "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val message = dc.document.toObject(Message::class.java)
                        val user = ChatMessage(
                            parentMessageId = message.parentMessageId,
                            senderId = message.senderId,
                            response = "",
                            prompt = message.prompt,
                            timestampFull = message.timestampFull,
                            timestampShort = message.timestampShort,
                            type = MessageType.USER
                        )
                        updateUIWithMessage(user)
                        val bot = ChatMessage(
                            parentMessageId = message.parentMessageId,
                            senderId = message.senderId,
                            response = message.response,
                            prompt = "",
                            timestampFull = message.timestampFull,
                            timestampShort = message.timestampShort,
                            type = MessageType.BOT
                        )
                        updateUIWithMessage(bot)
                    }
                }
            }
    }

    private fun updateUIWithMessage(message: ChatMessage) {
        chatMessages.add(message)
        adapter.notifyItemInserted(chatMessages.size - 1)
        binding.rvChat.scrollToPosition(chatMessages.size - 1)
    }

    private fun updateResponse(message: ChatMessage) {
        adapter.removeLastItem()
        chatMessages.add(message)
        adapter.notifyItemInserted(chatMessages.size - 1)
        binding.rvChat.scrollToPosition(chatMessages.size - 1)
        progressDialog.dismiss()
    }

}