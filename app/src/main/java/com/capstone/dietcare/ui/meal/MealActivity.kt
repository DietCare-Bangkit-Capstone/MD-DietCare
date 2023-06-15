package com.capstone.dietcare.ui.meal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.ui.main.MainActivity
import com.capstone.dietcare.R
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.data.local.login.PreferenceViewModel
import com.capstone.dietcare.databinding.ActivityMealBinding
import com.capstone.dietcare.ui.history.HistoryActivity
import com.capstone.dietcare.ui.profile.ProfileActivity
import com.capstone.dietcare.ui.progress.ProgressActivity
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MealActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMealBinding
    private lateinit var prefViewModel : PreferenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTabs()
        bottomNav()
    }


    private fun setTabs() {
        prefViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[PreferenceViewModel::class.java]

        prefViewModel.isLogin().observe(this){
            val sectionsPagerAdapter = MealSectionsPagerAdapter(this)

            Log.d("Meal0", "${it?.email}, ${it?.password}")

            sectionsPagerAdapter.email = it.email
            sectionsPagerAdapter.password = it.password

            binding.pagerMeal.adapter = sectionsPagerAdapter
            TabLayoutMediator(binding.tabMeal, binding.pagerMeal) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
            supportActionBar?.title = "Find Your Food Recommendation"
        }
    }

    private fun bottomNav(){
        binding.navBottom.selectedItemId = R.id.bnmNone
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

                R.id.bnmNone ->{
                    item.isEnabled = false
                }

                R.id.bnmHistory -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                }

                R.id.bnmProgress -> {
                    val intent = Intent(this, ProgressActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                }

                R.id.bnmProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
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