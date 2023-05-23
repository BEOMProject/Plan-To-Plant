package com.example.plantoplant

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantoplant.databinding.ActivityMainBinding
import com.example.plantoplant.navigation.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val intent = intent
        val userId = intent.getStringExtra("email")
        Toast.makeText(this, "환영합니다 ${userId}님!",
            Toast.LENGTH_SHORT).show()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Bottom Navigation View
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.action_calender
    }
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val uId = intent.getStringExtra("email").toString()
        val todayFragment = TodayFragment()
        val bundle = Bundle()
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
                bundle.putString("email", uId)
                todayFragment.arguments = bundle
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
    private var backPressedTime: Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - backPressedTime >= 2000) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
        }
        println("뒤로가기 버튼 클릭")
        super.onBackPressed()
    }
}