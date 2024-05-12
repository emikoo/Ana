package com.example.ana.presentation.ui.fragments.main.care

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.databinding.FragmentSelfCareBinding
import com.example.ana.presentation.ui.adapters.CareAdapter
import com.teenteen.teencash.presentation.base.BaseFragment

class SelfCareFragment : BaseFragment<FragmentSelfCareBinding>() {

    private lateinit var adapter: CareAdapter
    override fun setupViews() {
        setupRecyclerView()
    }

    fun setupRecyclerView() {
        adapter = CareAdapter()
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

}