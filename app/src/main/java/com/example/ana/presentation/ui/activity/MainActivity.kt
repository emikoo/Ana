package com.example.ana.presentation.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.databinding.ActivityMainBinding
import com.example.ana.presentation.extensions.updateLanguage
import com.example.ana.presentation.utills.checkInternetConnection
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var prefs: PrefsSettings

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Ana)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PrefsSettings(this)
        PreferenceManager(this).updateLanguage(prefs.getSettingsLanguage() , this, prefs)
        checkInternetConnection(this::setupNavigation , this , this::noConnection)
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
    private fun noConnection() {
        Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
    }
}