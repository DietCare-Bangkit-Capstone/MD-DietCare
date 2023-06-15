package com.capstone.dietcare.ui.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.dietcare.R
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.databinding.ActivityHistoryBinding
import com.capstone.dietcare.ui.main.MainActivity
import com.capstone.dietcare.ui.meal.MealActivity
import com.capstone.dietcare.ui.profile.ProfileActivity
import com.capstone.dietcare.ui.progress.ProgressActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Recommendation History"

        bottomNav()
        setViewModel()
        setDelete()
    }

    private fun setDelete() {
        val idIntent = intent.getIntExtra("DELETE_INTENT", 0)
        historyViewModel.delete(idIntent)
        historyViewModel.getAllHistory().observe(this){history ->
            binding.rvHistory.adapter = HistoryAdapter(history)
            binding.rvHistory.setHasFixedSize(true)
            binding.rvHistory.layoutManager = LinearLayoutManager(this)

        }

    }

    private fun setViewModel() {
        historyViewModel.getAllHistory().observe(this){history ->
            binding.rvHistory.adapter = HistoryAdapter(history)
            binding.rvHistory.setHasFixedSize(true)
            binding.rvHistory.layoutManager = LinearLayoutManager(this)

        }
    }


    private fun bottomNav(){
        binding.bnmMeal.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()
        }
        binding.navBottom.selectedItemId = R.id.bnmHistory
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
}