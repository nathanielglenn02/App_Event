package com.example.app_event.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.app_event.R
import com.example.app_event.data.ThemePreferences
import com.example.app_event.data.dataStore
import com.example.app_event.viewmodel.SettingsViewModel
import com.example.app_event.viewmodel.SettingsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ThemePreferences dan SettingsViewModel
        val preferences = ThemePreferences.getInstance(applicationContext.dataStore)
        val factory = SettingsViewModelFactory(preferences)
        settingsViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        // Observe pengaturan tema sebelum menampilkan layout
        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Set layout setelah tema diterapkan
        setContentView(R.layout.activity_main)

        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_active_events, R.id.navigation_finished_events)
        )
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_active_events -> {
                    openFragment(ActiveEventsFragment())
                    true
                }
                R.id.nav_finished_events -> {
                    openFragment(FinishedEventsFragment())
                    true
                }
                R.id.nav_home_events -> {
                    openFragment(HomeFragment())
                    true
                }
//                R.id.nav_search_event -> {
//                    openFragment(SearchFragment())
//                    true
//                }
                R.id.nav_favourite_events -> {
                    openFragment(FavouriteFragment())
                    true
                }
                R.id.nav_settings_events -> {
                    openFragment(SettingsFragment())
                    true
                }

                else -> false
            }
        }
        if (savedInstanceState == null) {
            openFragment(HomeFragment())
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
