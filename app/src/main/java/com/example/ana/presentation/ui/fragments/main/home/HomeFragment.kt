package com.example.ana.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.data.model.Child
import com.example.ana.databinding.FragmentHomeBinding
import com.example.ana.presentation.ui.adapters.AdviceAdapter
import com.example.ana.presentation.ui.adapters.ChildAdapter
import com.example.ana.presentation.ui.fragments.main.home.child.ChildAuthBottomSheet
import com.example.ana.presentation.ui.fragments.main.home.child.UpdateData
import com.example.ana.view_model.HomeViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), UpdateData {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ChildAdapter
    private lateinit var children: MutableList<Child>
    override fun attachBinding(
        list: MutableList<FragmentHomeBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHomeBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun setupViews() {
        progressDialog.show()
        children = mutableListOf()
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.title.text = "Hello, ${prefs.getName()}!"
        setupRecyclerView()
        setupButtons()
        viewModel.getChildren(currentUser!!.uid)
    }

    private fun setupRecyclerView() {
        binding.listAdvices.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adviceAdapter = AdviceAdapter(5)
        val popularAdapter = PopularAdapter(5)
        binding.listAdvices.adapter = adviceAdapter
        binding.listPopular.adapter = popularAdapter
    }

    private fun setupButtons() {
        binding.diary
        binding.book
        binding.expert
        binding.plan
    }

    override fun subscribeToLiveData() {
        viewModel.children.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutItems.visibility = View.GONE
                progressDialog.dismiss()
                binding.btnAddFirstChild.setOnClickListener {
                    ChildAuthBottomSheet(this).show(childFragmentManager, "Authorization")
                }
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutItems.visibility = View.VISIBLE
                progressDialog.dismiss()
                children = it.toMutableList()
                adapter = ChildAdapter(children)
                binding.rvChildren.adapter = adapter
                binding.rvChildren.layoutManager = LinearLayoutManager(requireContext())

                binding.btnAddBaby.setOnClickListener {
                    ChildAuthBottomSheet(this).show(childFragmentManager, "Authorization")
                }

                binding.addBabyTitle.setOnClickListener {
                    ChildAuthBottomSheet(this).show(childFragmentManager, "Authorization")
                }
            }
        }
    }

    override fun updateChildrenList() {
        viewModel.getChildren(currentUser!!.uid)
    }
}
