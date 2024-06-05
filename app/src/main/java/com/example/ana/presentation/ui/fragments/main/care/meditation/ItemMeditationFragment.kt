package com.example.ana.presentation.ui.fragments.main.care.meditation

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
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
import java.io.IOException


class ItemMeditationFragment : BaseFragment<FragmentItemMeditationBinding>(), SoundPlayer {

    private lateinit var viewModel: CareViewModel
    private lateinit var adapter: SoundSessionAdapter
    private lateinit var soundSession: MutableList<SoundSession>
    private lateinit var mediaPlayer: MediaPlayer
    private var count = 0

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
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.name.text = getString(R.string.savor_the_moment)
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
            binding.totalSessions.text = "${it.sessions}" + getString(R.string.sessions)
            count = it.count
            binding.sessions.text = "${it.count}/${it.sessions}"
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
        progressDialog.show()
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(requireContext(), soundUrl.toUri())
                setOnPreparedListener {
                    it.start()
                    progressDialog.dismiss()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("MediaPlayer", "Error occurred: what=$what, extra=$extra")
                    progressDialog.dismiss()
                    true
                }
                prepareAsync()
            }
        } catch (e: IOException) {
            Log.e("MediaPlayer", "IOException: ${e.message}")
            progressDialog.dismiss()
        } catch (e: IllegalArgumentException) {
            Log.e("MediaPlayer", "IllegalArgumentException: ${e.message}")
            progressDialog.dismiss()
        } catch (e: SecurityException) {
            Log.e("MediaPlayer", "SecurityException: ${e.message}")
            progressDialog.dismiss()
        } catch (e: IllegalStateException) {
            Log.e("MediaPlayer", "IllegalStateException: ${e.message}")
            progressDialog.dismiss()
        }
    }

    override fun soundStop() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun increaseCount(soundId: Int) {
        viewModel.updateSessionCount(count + 1)
        viewModel.updateSoundTried(soundId)
    }
}