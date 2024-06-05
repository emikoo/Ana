package com.example.ana.presentation.ui.fragments.main.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.data.model.ChatMessage
import com.example.ana.data.model.MessageType
import com.example.ana.databinding.FragmentChatBinding
import com.example.ana.presentation.ui.adapters.MessageAdapter
import com.example.ana.view_model.MessagesViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.teenteen.teencash.presentation.base.BaseFragment

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private lateinit var adapter: MessageAdapter
    private lateinit var chatMessages: MutableList<ChatMessage>
    private lateinit var viewModel: MessagesViewModel
    private var pressed = false
    private var messageListener: ListenerRegistration? = null

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
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getMessages(prefs.getCurrentUserId())
        binding.send.setOnClickListener {
            val messageText = binding.etMessage.text.trim().toString()
            if (messageText.isNotEmpty()) {
                progressDialog.show()
                viewModel.sendMessage(prefs.getCurrentUserId(), messageText)
                viewModel.monitorLastMessage(prefs.getCurrentUserId(), messageText)
                binding.etMessage.setText("")
            }
            pressed = true
        }
    }

    override fun subscribeToLiveData() {
        viewModel.messages.observe(viewLifecycleOwner) {
            progressDialog.dismiss()
            if (messageListener == null) {
                setupMessageListener()
            }
        }

        viewModel.lastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                if (pressed) {
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
    }

    private fun setupMessageListener() {
        messageListener?.remove()
        messageListener =
            db.collection("users").document(prefs.getCurrentUserId()).collection("messages")
                .orderBy("timestampFull")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("ChatFragment", "listen:error", e)
                        return@addSnapshotListener
                    }

                    for (dc in snapshots!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val message = dc.document.toObject(ChatMessage::class.java)
                            handleNewMessage(message)
                        }
                    }
                }
    }

    private fun handleNewMessage(message: ChatMessage) {
        if (!chatMessages.any { it.timestampFull == message.timestampFull && it.senderId == message.senderId && it.prompt == message.prompt && it.response == message.response }) {
            if (message.prompt.isNotEmpty()) {
                val userMessage = ChatMessage(
                    parentMessageId = message.parentMessageId,
                    senderId = message.senderId,
                    response = "",
                    prompt = message.prompt,
                    timestampFull = message.timestampFull,
                    timestampShort = message.timestampShort,
                    type = MessageType.USER
                )
                updateUIWithMessage(userMessage)
            }

            if (message.response.isNotEmpty()) {
                val botMessage = ChatMessage(
                    parentMessageId = message.parentMessageId,
                    senderId = message.senderId,
                    response = message.response,
                    prompt = "",
                    timestampFull = message.timestampFull,
                    timestampShort = message.timestampShort,
                    type = MessageType.BOT
                )
                updateUIWithMessage(botMessage)
            }
        }
    }

    private fun updateUIWithMessage(message: ChatMessage) {
        chatMessages.add(message)
        adapter.notifyItemInserted(chatMessages.size - 1)
        binding.rvChat.scrollToPosition(chatMessages.size - 1)
//        progressDialog.dismiss()
    }

    private fun updateResponse(message: ChatMessage) {
        chatMessages.add(message)
        adapter.notifyItemInserted(chatMessages.size - 1)
        binding.rvChat.scrollToPosition(chatMessages.size - 1)
        progressDialog.dismiss()
    }

    override fun onPause() {
        super.onPause()
        pressed = false
        messageListener?.remove()
        messageListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatMessages.clear()
        adapter.notifyDataSetChanged()
    }
}
