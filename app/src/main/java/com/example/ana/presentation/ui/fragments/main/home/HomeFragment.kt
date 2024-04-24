package com.example.ana.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.databinding.FragmentHomeBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun attachBinding(
        list: MutableList<FragmentHomeBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHomeBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun setupViews() {
        binding.title.text = "Hello, ${prefs.getName()}!"
        setupRecyclerView()
        if (prefs.getChildName().isNullOrEmpty() && prefs.getChildAge().isNullOrEmpty()) {
            binding.btnAddChild.visibility = View.VISIBLE
            binding.btnAddChild.setOnClickListener {
                val directions =
                    HomeFragmentDirections.actionHomeFragmentToChildAuthFragment()
                findNavController().navigate(directions)
            }
        } else {
            binding.btnAddChild.visibility = View.INVISIBLE
            binding.ivAdd.visibility = View.INVISIBLE
            binding.btnText.visibility = View.INVISIBLE
            binding.childCard.visibility = View.VISIBLE
            binding.childName.text = prefs.getChildName()
            binding.childAge.text = "${prefs.getChildAge()}"
        }
    }

    fun setupRecyclerView() {
        binding.listAdvices.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listPopular.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adviceAdapter = AdviceAdapter(5)
        val popularAdapter = PopularAdapter(5)
        binding.listAdvices.adapter = adviceAdapter
        binding.listPopular.adapter = popularAdapter
    }

    override fun subscribeToLiveData() {}
}
