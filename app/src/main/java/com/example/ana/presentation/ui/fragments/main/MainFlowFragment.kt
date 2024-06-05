package com.example.ana.presentation.ui.fragments.main

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ana.R
import com.example.ana.databinding.FlowFragmentMainBinding
import com.example.ana.presentation.base.BaseFlowFragment
import com.example.ana.presentation.extensions.isKeyboardVisible

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main, R.id.nav_host_fragment_main
) {
    private val binding by viewBinding(FlowFragmentMainBinding::bind)
    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        Log.d("MainFlowFragment", "Keyboard visibility check")
        if (isKeyboardVisible()) {
            Log.d("MainFlowFragment", "Keyboard is visible")
            binding.bottomNavigation.isVisible = false
        } else {
            Log.d("MainFlowFragment", "Keyboard is not visible")
            binding.bottomNavigation.isVisible = true
        }
    }

    override fun setupNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}