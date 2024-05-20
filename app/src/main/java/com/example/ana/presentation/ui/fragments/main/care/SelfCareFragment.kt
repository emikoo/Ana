package com.example.ana.presentation.ui.fragments.main.care

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.R
import com.example.ana.databinding.FragmentSelfCareBinding
import com.example.ana.presentation.ui.adapters.CareAdapter
import com.example.ana.presentation.ui.adapters.CareSelector
import com.example.ana.presentation.ui.fragments.main.home.HomeFragmentDirections
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
        binding.btnNotification.setOnClickListener {
            findNavController().navigate(SelfCareFragmentDirections.actionSelfCareFragmentToNotificationFragment())
        }
        binding.premiumView.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
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
        findNavController().navigate(SelfCareFragmentDirections.actionSelfCareFragmentToMeditationFragment())
    }

    override fun openPodcasts() {
        findNavController().navigate(SelfCareFragmentDirections.actionSelfCareFragmentToPodcastFragment())
    }

    override fun openWishCard() {
        findNavController().navigate(SelfCareFragmentDirections.actionSelfCareFragmentToWishCardFragment())
    }

    override fun premium() {
        Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
    }

}