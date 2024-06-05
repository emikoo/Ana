package com.example.ana.presentation.ui.fragments.main.care.podcast

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Podcast
import com.example.ana.databinding.FragmentItemPodcastBinding
import com.example.ana.presentation.ui.adapters.PodcastAdapter
import com.example.ana.presentation.ui.adapters.PodcastInterface
import com.example.ana.view_model.CareViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.teenteen.teencash.presentation.base.BaseFragment

class ItemPodcastFragment : BaseFragment<FragmentItemPodcastBinding>(), PodcastInterface {

    private lateinit var adapter: PodcastAdapter
    private lateinit var podcastList: MutableList<Podcast>
    private lateinit var viewModel: CareViewModel
    private var player: ExoPlayer? = null
    private var isPlayed = false

    private val arguments: ItemPodcastFragmentArgs by navArgs()
    override fun setupViews() {
        progressDialog.show()
        viewModel = ViewModelProvider(requireActivity())[CareViewModel::class.java]
        podcastList = mutableListOf()
        adapter = PodcastAdapter(podcastList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        viewModel.getItemPodcast(arguments.itemId)
        viewModel.getPodcastExceptItem(arguments.itemId + 1)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(ItemPodcastFragmentDirections.actionItemPodcastFragment2ToPodcastFragment())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subscribeToLiveData() {
        viewModel.itemPodcast.observe(viewLifecycleOwner) {
            val podcast = it
            binding.name.text = podcast.name
            Glide.with(this)
                .load(podcast.preview)
                .placeholder(R.drawable.ic_logo)
                .into(binding.preview)
            binding.author.text = podcast.author
            viewModel.updateViews(podcast.id, podcast.views + 1)
            binding.views.text = "${podcast.views} " + getString(R.string.views)
            progressDialog.dismiss()
            showVideo(podcast.url)
        }
        viewModel.podcastExcept.observe(viewLifecycleOwner) {
            podcastList.clear()
            podcastList.addAll(it)
            adapter.notifyDataSetChanged()
            progressDialog.dismiss()
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentItemPodcastBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentItemPodcastBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun onPodcastSelected(podcast: Podcast) {
        progressDialog.show()
        viewModel.getItemPodcast(podcast.id - 1)
        viewModel.getPodcastExceptItem(podcast.id)
        binding.play.visibility = View.VISIBLE
        binding.preview.visibility = View.VISIBLE
        binding.shadow.visibility = View.VISIBLE
    }

    private fun showVideo(podcastUrl: String?) {
        binding.play.setOnClickListener {
            if (!isPlayed) {
                initializePlayer(podcastUrl.toString())
                binding.play.visibility = View.INVISIBLE
                binding.preview.visibility = View.INVISIBLE
                binding.shadow.visibility = View.INVISIBLE
            }
//            val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
//            intent.putExtra("PODCAST", podcastUrl)
//            startActivity(intent)
        }
    }

    private fun initializePlayer(url: String) {
        player = ExoPlayer.Builder(requireContext()).build().also {
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