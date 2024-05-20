package com.example.ana.presentation.ui.fragments.main.home.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ana.databinding.FragmentChildDetailBinding
import com.example.ana.presentation.extensions.ageOfChild
import com.example.ana.view_model.AdviceViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class ChildDetailFragment : BaseFragment<FragmentChildDetailBinding>() {

    private lateinit var viewModel: AdviceViewModel
    private val arguments: ChildDetailFragmentArgs by navArgs()

        override fun setupViews() {
        viewModel = ViewModelProvider(this)[AdviceViewModel::class.java]
        viewModel.getAgeOfChild(prefs.getCurrentUserId(), arguments.childName)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(ChildDetailFragmentDirections.actionChildDetailFragmentToHomeFragment())
        }
        setupCategories()
    }

    private fun setupCategories() {
        binding.name.text = arguments.childName
        binding.sleep.setOnClickListener {
            findNavController().navigate(ChildDetailFragmentDirections.actionChildDetailFragmentToArticleFragment("Sleep", arguments.childName))
        }
        binding.food.setOnClickListener {
            findNavController().navigate(ChildDetailFragmentDirections.actionChildDetailFragmentToArticleFragment("Food", arguments.childName))
        }
        binding.physicalActivity.setOnClickListener {
            findNavController().navigate(ChildDetailFragmentDirections.actionChildDetailFragmentToArticleFragment("Physical Activity", arguments.childName))
        }
        binding.mentalActivity.setOnClickListener {
            findNavController().navigate(ChildDetailFragmentDirections.actionChildDetailFragmentToArticleFragment("Mental Activity", arguments.childName))
        }
    }

    override fun subscribeToLiveData() {
        viewModel.ageOfChild.observe(viewLifecycleOwner) {
            binding.age.ageOfChild(it)
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentChildDetailBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentChildDetailBinding.inflate(layoutInflater, container, attachToRoot))
    }
}
