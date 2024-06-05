package com.example.ana.presentation.ui.fragments.main.care.meditation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.R
import com.example.ana.data.model.Meditation
import com.example.ana.databinding.FragmentMeditationBinding
import com.example.ana.presentation.ui.adapters.MeditationAdapter
import com.example.ana.presentation.ui.adapters.MeditationInterface
import com.example.ana.view_model.CareViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class MeditationFragment : BaseFragment<FragmentMeditationBinding>(), MeditationInterface {

    private lateinit var viewModel: CareViewModel
    private lateinit var adapter: MeditationAdapter
    private lateinit var meditationList: MutableList<Meditation>

    override fun setupViews() {
        progressDialog.show()
        meditationList = mutableListOf()
        viewModel = ViewModelProvider(this)[CareViewModel::class.java]
        viewModel.getMeditation()
        adapter = MeditationAdapter(meditationList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.btnBack.setOnClickListener {
            findNavController().navigate(MeditationFragmentDirections.actionMeditationFragmentToSelfCareFragment())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subscribeToLiveData() {
        viewModel.meditation.observe(viewLifecycleOwner) {
            meditationList.clear()
            meditationList.addAll(it)
            adapter.notifyDataSetChanged()
            progressDialog.dismiss()
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentMeditationBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentMeditationBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun onLockedSelected(meditation: Meditation) {
        Toast.makeText(
            requireContext(),
            getString(R.string.these_sessions_are_available_only_with_premium_subscription),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onUnlockedSelected(meditation: Meditation) {
        findNavController().navigate(
            MeditationFragmentDirections.actionMeditationFragmentToItemMeditationFragment(
                0
            )
        )
    }
}