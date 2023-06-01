package com.example.plantoplant

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantoplant.databinding.ActivityMainBinding
import com.example.plantoplant.navigation.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("email") ?: ""
        Toast.makeText(this, "환영합니다 $userId 님!", Toast.LENGTH_SHORT).show()

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.action_calender

        loadProfileFragment()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                loadProfileFragment()
                return true
            }
            R.id.action_calender -> {
                loadCalenderFragment()
                return true
            }
            R.id.action_addplan -> {
                loadAddPlanFragment()
                return true
            }
            R.id.action_today -> {
                loadTodayFragment()
                return true
            }
            R.id.action_plantEncyclopedia -> {
                loadPlantEncyclopediaFragment()
                return true
            }
        }
        return false
    }

    private fun loadProfileFragment() {
        val profileFragment = ProfileFragment()
        val bundle = Bundle()
        bundle.putString("email", userId)
        profileFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_content, profileFragment).commit()
    }

    private fun loadCalenderFragment() {
        val calenderFragment = CalenderFragment()
        val bundle = Bundle()
        bundle.putString("email", userId)
        calenderFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_content, calenderFragment).commit()
    }

    private fun loadAddPlanFragment() {
        val addPlanFragment = AddPlanFragment()
        val bundle = Bundle()
        bundle.putString("email", userId)
        addPlanFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_content, addPlanFragment).commit()
    }

    private fun loadTodayFragment() {
        val todayFragment = TodayFragment()
        val bundle = Bundle()
        bundle.putString("email", userId)
        todayFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_content, todayFragment).commit()
    }

    private fun loadPlantEncyclopediaFragment() {
        val plantEncyclopediaFragment = PlantBookFragment()
        val bundle = Bundle()
        bundle.putString("email", userId)
        plantEncyclopediaFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_content, plantEncyclopediaFragment).commit()
    }

    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime >= 2000) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}