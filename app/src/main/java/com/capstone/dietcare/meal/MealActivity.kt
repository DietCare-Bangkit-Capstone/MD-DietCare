package com.capstone.dietcare.meal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.capstone.dietcare.main.MainActivity
import com.capstone.dietcare.R
import com.capstone.dietcare.databinding.ActivityMealBinding
import com.capstone.dietcare.history.HistoryActivity
import com.google.android.material.tabs.TabLayoutMediator

class MealActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTabs()
        bottomNav()
    }

    private fun setTabs() {
        val sectionsPagerAdapter = MealSectionsPagerAdapter(this)
        binding.pagerMeal.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabMeal, binding.pagerMeal) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Find Your Food Recommendation"
    }

    private fun bottomNav(){
        binding.navBottom.selectedItemId = R.id.bnmMeal
        binding.navBottom.isItemHorizontalTranslationEnabled = true
        binding.navBottom.setOnNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.bnmHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                }

                R.id.bnmHistory -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                }

                R.id.bnmProgress -> {

                }

                R.id.bnmProfile -> {

                }
            }
            true
        }
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab1meal,
            R.string.tab2meal
        )
    }
}