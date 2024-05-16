package com.example.ana.presentation.ui.fragments.main.care.podcast

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ana.R
import com.example.ana.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Ana)
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("PODCAST")

        binding.btnPlayVideo.setOnClickListener {
            initializePlayer(videoUrl.toString())
            binding.btnPlayVideo.visibility = View.GONE
        }

        binding.btnBack.setOnClickListener {
            this.finish()
        }
    }

    private fun initializePlayer(url: String) {
        player = ExoPlayer.Builder(this).build().also {
            binding.playerView.player = it
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            it.setMediaItem(mediaItem)
            it.prepare()
            it.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}
