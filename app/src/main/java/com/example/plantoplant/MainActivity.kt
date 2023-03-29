package com.example.plantoplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.plantoplant.databinding.ActivityMainBinding
import com.example.plantoplant.navigation.CalenderFragment
import com.example.plantoplant.navigation.PlantEncyclopediaFragment
import com.example.plantoplant.navigation.ProfileFragment
import com.example.plantoplant.navigation.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.action_profile -> {
                var profileFragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, profileFragment).commit()
                return true
            }
            R.id.action_calender -> {
                var calenderFragment = CalenderFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, calenderFragment).commit()
                return true
            }
            R.id.action_today -> {
                var todayFragment = TodayFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, todayFragment).commit()
                return true
            }
            R.id.action_plantEncyclopedia -> {
                var plantEncyclopediaFragment = PlantEncyclopediaFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, plantEncyclopediaFragment).commit()
                return true
            }
        }
        return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
    }
}