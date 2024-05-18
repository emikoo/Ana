package com.example.ana.presentation.ui.fragments.main.care.meditation

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.SoundSession
import com.example.ana.databinding.FragmentItemMeditationBinding
import com.example.ana.presentation.ui.adapters.SoundPlayer
import com.example.ana.presentation.ui.adapters.SoundSessionAdapter
import com.example.ana.view_model.CareViewModel
import com.teenteen.teencash.presentation.base.BaseFragment


class ItemMeditationFragment : BaseFragment<FragmentItemMeditationBinding>(), SoundPlayer {

    private lateinit var viewModel: CareViewModel
    private lateinit var adapter: SoundSessionAdapter
    private lateinit var soundSession: MutableList<SoundSession>
    private lateinit var mediaPlayer: MediaPlayer

    private val arguments: ItemMeditationFragmentArgs by navArgs()
    private var flag: Boolean = false

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[CareViewModel::class.java]
        viewModel.getItemMeditation(arguments.itemId)
        viewModel.getSoundSession()
        setupRecyclerView()
        binding.btnBack.setOnClickListener { goBack() }
        binding.readMore.setOnClickListener { showFullDescription() }
    }

    private fun setupRecyclerView() {
        soundSession = mutableListOf()
        adapter = SoundSessionAdapter(soundSession, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun goBack() {
        findNavController().navigate(ItemMeditationFragmentDirections.actionItemMeditationFragmentToMeditationFragment())
    }

    private fun showFullDescription() {
        flag = if (!flag) {
            binding.description.setText(R.string.description_full)
            binding.readMore.setText(R.string.read_less)
            true
        } else {
            binding.description.setText(R.string.description_short)
            binding.readMore.setText(R.string.read_more)
            false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subscribeToLiveData() {
        viewModel.itemMeditation.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it.preview)
                .placeholder(R.drawable.ic_logo)
                .into(binding.preview)
            binding.totalSessions.text = "${it.sessions} sessions"
            binding.sessions.text = "${it.count}/${it.sessions}"
            binding.name.text = it.name
//            viewModel.updateSessionCount(it.count +1)
        }
        viewModel.soundSession.observe(viewLifecycleOwner) {
            soundSession.clear()
            soundSession.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentItemMeditationBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentItemMeditationBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun soundPlay(soundUrl: String) {
//        mediaPlayer = MediaPlayer().apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .build()
//            )
//            setDataSource(requireContext(), soundUrl.toUri())
//            prepare()
//            start()
//        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}