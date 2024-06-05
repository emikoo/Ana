package com.example.ana.presentation.ui.fragments.main.care.podcast

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ana.data.model.Podcast
import com.example.ana.databinding.FragmentPodcastBinding
import com.example.ana.presentation.ui.adapters.PodcastAdapter
import com.example.ana.presentation.ui.adapters.PodcastInterface
import com.example.ana.view_model.CareViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class PodcastFragment : BaseFragment<FragmentPodcastBinding>(), PodcastInterface {
    private lateinit var viewModel: CareViewModel
    private lateinit var adapter: PodcastAdapter
    private lateinit var podcastList: MutableList<Podcast>

    override fun setupViews() {
        progressDialog.show()
        podcastList = mutableListOf()
        viewModel = ViewModelProvider(this)[CareViewModel::class.java]
        viewModel.getPodcast()
        adapter = PodcastAdapter(podcastList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.btnBack.setOnClickListener {
            val directions = PodcastFragmentDirections.actionPodcastFragmentToSelfCareFragment()
            findNavController().navigate(directions)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subscribeToLiveData() {
        viewModel.podcast.observe(viewLifecycleOwner) {
            podcastList.addAll(it)
            adapter.notifyDataSetChanged()
            progressDialog.dismiss()
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentPodcastBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentPodcastBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun onPodcastSelected(podcast: Podcast) {
        val directions =
            PodcastFragmentDirections.actionPodcastFragmentToItemPodcastFragment2(podcast.id - 1)
        findNavController().navigate(directions)
    }
}