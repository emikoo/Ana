package com.example.ana.presentation.ui.fragments.main.care.podcast

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Podcast
import com.example.ana.databinding.FragmentPodcastItemBinding
import com.example.ana.presentation.ui.adapters.PodcastAdapter
import com.example.ana.presentation.ui.adapters.PodcastInterface
import com.example.ana.view_model.CareViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class ItemPodcastFragment : BaseFragment<FragmentPodcastItemBinding>(), PodcastInterface {

    private lateinit var adapter: PodcastAdapter
    private lateinit var podcastList: MutableList<Podcast>
    private lateinit var viewModel: CareViewModel

    private val arguments: ItemPodcastFragmentArgs by navArgs()
    override fun setupViews() {
        progressDialog.show()
        viewModel = ViewModelProvider(requireActivity())[CareViewModel::class.java]
        podcastList = mutableListOf()
        adapter = PodcastAdapter(podcastList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        viewModel.getItemPodcast(arguments.itemPodcast)
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
            viewModel.updateViews(podcast.id, podcast.views+1)
            binding.views.text = "${podcast.views} views"
            viewModel.getPodcastExceptItem(arguments.itemPodcast)
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
        list: MutableList<FragmentPodcastItemBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentPodcastItemBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun onPodcastSelected(podcast: Podcast) {
        progressDialog.show()
        viewModel.getItemPodcast(podcast.id-1)
    }

    private fun showVideo(podcastUrl: String?) {
        binding.play.setOnClickListener {
            val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
            intent.putExtra("PODCAST", podcastUrl)
            startActivity(intent)
        }
    }
}