package com.example.ana.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var prefs: PrefsSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Ana)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PrefsSettings(this)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        when (prefs.isFirstTimeLaunch()) {
            PrefsSettings.FIRST_TIME -> {
                navGraph.setStartDestination(R.id.onboardingFlowFragment)
            }
            PrefsSettings.NOT_AUTH -> {
                navGraph.setStartDestination(R.id.authFlowFragment)
            }
            PrefsSettings.USER -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
        }
        navController.graph = navGraph
    }
}