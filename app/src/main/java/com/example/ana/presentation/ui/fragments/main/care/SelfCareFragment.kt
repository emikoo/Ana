package com.example.ana.presentation.ui.fragments.main.care

import android.R
import android.R.attr.fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.databinding.FragmentSelfCareBinding
import com.example.ana.presentation.ui.adapters.CareAdapter
import com.example.ana.presentation.ui.adapters.CareSelector
import com.teenteen.teencash.presentation.base.BaseFragment


class SelfCareFragment : BaseFragment<FragmentSelfCareBinding>(), CareSelector {

    private lateinit var adapter: CareAdapter
    override fun setupViews() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = CareAdapter(this)
        binding.buttons.adapter = adapter
        binding.buttons.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun subscribeToLiveData() {
    }

    override fun attachBinding(
        list: MutableList<FragmentSelfCareBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentSelfCareBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun openMeditation() {
        val directions = SelfCareFragmentDirections.actionSelfCareFragmentToMeditationFragment()
        findNavController().navigate(directions)
    }

    override fun openPodcasts() {
        val directions = SelfCareFragmentDirections.actionSelfCareFragmentToPodcastFragment()
        findNavController().navigate(directions)
    }

}