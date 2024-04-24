package com.example.ana.presentation.ui.fragments.main

import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ana.R
import com.example.ana.databinding.FlowFragmentMainBinding
import com.example.ana.presentation.base.BaseFlowFragment

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main , R.id.nav_host_fragment_main
) {
    private val binding by viewBinding(FlowFragmentMainBinding::bind)

    override fun setupNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
    }
}