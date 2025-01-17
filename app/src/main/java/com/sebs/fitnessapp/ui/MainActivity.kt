package com.sebs.fitnessapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.ui.fragments.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Cargar fragmento inicial (Men)
        loadFragment(LegendListFragment())

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_men -> loadFragment(LegendListFragment())
                R.id.nav_women -> loadFragment(WomenFragment())
                R.id.nav_ranking -> loadFragment(RankingFragment())
                R.id.nav_map -> loadFragment(MapFragment())
                R.id.nav_profile -> loadFragment(UserLegendFragment()) // Cambio aquí
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}