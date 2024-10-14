package com.example.app_event.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.app_event.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                R.id.nav_search_event -> {
                    openFragment(SearchFragment())
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
