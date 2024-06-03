package com.example.ana.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.data.model.Advice
import com.example.ana.databinding.FragmentArticleBinding
import com.example.ana.presentation.extensions.ageOfChild
import com.example.ana.presentation.ui.adapters.ArticleAdapter
import com.example.ana.view_model.AdviceViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class ArticleFragment : BaseFragment<FragmentArticleBinding>() {
    private lateinit var viewModel: AdviceViewModel
    private lateinit var adapter: ArticleAdapter
    private lateinit var adviceList: MutableList<Advice>
    private val arguments: ArticleFragmentArgs by navArgs()

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[AdviceViewModel::class.java]

        binding.categoryName.text = arguments.categoryName
        adviceList = mutableListOf()
        adapter = ArticleAdapter(adviceList)
        binding.articlesList.adapter = adapter
        binding.articlesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (arguments.childName != "") {
            viewModel.getAgeOfChild(prefs.getCurrentUserId(), arguments.childName)
            callFromChildDetailFragment()
        }
        else {
            viewModel.getArticlesByCategory(prefs.getSettingsLanguage(), arguments.categoryName)
            callFromHomeFragment()
        }
    }

    private fun callFromChildDetailFragment() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(
                ArticleFragmentDirections.actionArticleFragmentToChildDetailFragment(
                    arguments.childName, ""
                )
            )
        }
    }

    private fun callFromHomeFragment() {
        binding.ageOfChild.visibility = View.GONE
        binding.btnBack.setOnClickListener {
            findNavController().navigate(
                ArticleFragmentDirections.actionArticleFragmentToHomeFragment()
            )
        }
    }

    override fun subscribeToLiveData() {
        viewModel.ageOfChild.observe(viewLifecycleOwner) {
            if (arguments.childName != "") {
                binding.ageOfChild.ageOfChild(it)
                viewModel.getArticlesByCategoryAndChildBirthdate(arguments.categoryName, it)
            } else {
                viewModel.getArticlesByCategory(prefs.getSettingsLanguage(), arguments.categoryName)
            }
        }
        viewModel.articlesByChild.observe(viewLifecycleOwner) {
            adapter = ArticleAdapter(it.toMutableList())
            binding.articlesList.adapter = adapter
            binding.articlesList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        viewModel.articles.observe(viewLifecycleOwner) {
            adapter = ArticleAdapter(it.toMutableList())
            binding.articlesList.adapter = adapter
            binding.articlesList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentArticleBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentArticleBinding.inflate(layoutInflater, container, attachToRoot))
    }
}