package com.example.plantoplant

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.plantoplant.databinding.ActivityMainBinding
import com.example.plantoplant.navigation.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Bottom Navigation View
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.action_calender
    }
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
            R.id.action_addplan -> {
                var AddPlanFragment = AddPlanFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, AddPlanFragment).commit()
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
}