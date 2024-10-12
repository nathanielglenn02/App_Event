package com.example.app_event.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.app_event.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Bottom Navigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Atur AppBarConfiguration untuk fragment yang tidak memerlukan back button
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_active_events, R.id.navigation_finished_events)
        )

        // Listener untuk item yang dipilih di Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_active_events -> {
                    // Menampilkan ActiveEventsFragment
                    openFragment(ActiveEventsFragment())
                    true
                }
                R.id.nav_finished_events -> {
                    // Menampilkan FinishedEventsFragment (yang akan kita buat di langkah berikutnya)
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

        // Menampilkan fragment default (ActiveEventsFragment) saat pertama kali aplikasi dibuka
        if (savedInstanceState == null) {
            openFragment(HomeFragment())
        }
    }

    // Fungsi untuk membuka fragment
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
