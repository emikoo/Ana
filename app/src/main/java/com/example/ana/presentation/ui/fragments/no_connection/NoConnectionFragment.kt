package com.example.ana.presentation.ui.fragments.no_connection

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ana.databinding.FragmentNoConnectionBinding
import com.example.ana.presentation.ui.activity.MainActivity
import com.teenteen.teencash.presentation.base.BaseFragment

class NoConnectionFragment : BaseFragment<FragmentNoConnectionBinding>() {
    override fun setupViews() {
        binding.btnRetry.setOnClickListener {
            val intent = Intent(requireContext() , MainActivity::class.java)
            startActivity(intent)
            activity?.finishAffinity()
        }
    }

    override fun subscribeToLiveData() {}

    override fun attachBinding(
        list: MutableList<FragmentNoConnectionBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentNoConnectionBinding.inflate(layoutInflater , container , attachToRoot))
    }
}