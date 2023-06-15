package com.capstone.dietcare.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.R
import com.capstone.dietcare.ui.auth.LoginActivity
import com.capstone.dietcare.data.helper.DateHelper
import com.capstone.dietcare.data.helper.DateHelper.withNewsDateFormat
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.data.local.login.PreferenceViewModel
import com.capstone.dietcare.data.remote.profile.DataItem
import com.capstone.dietcare.databinding.ActivityProfileBinding
import com.capstone.dietcare.ui.history.HistoryActivity
import com.capstone.dietcare.ui.main.MainActivity
import com.capstone.dietcare.ui.meal.MealActivity
import com.capstone.dietcare.ui.progress.ProgressActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var prefViewModel : PreferenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Profile"

        setViewModel()
        setProfileIntent()
        bottomNav()
    }

    private fun setProfileIntent() {
        binding.btnPasswordUpdate.setOnClickListener {
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.btnUserDelete.setOnClickListener {
            val intent = Intent(this, DeleteProfileActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Logout from DietCare")
                setMessage("Are you sure you want to logout from the application? You can log back in at any time with your registered account.")
                setPositiveButton("Logout"){_, _->
                    prefViewModel.logout()
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun setViewModel() {
        prefViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[PreferenceViewModel::class.java]
        profileViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
        prefViewModel.isLogin().observe(this){
            profileViewModel.getUserDetail(it.email)
        }
        profileViewModel.itemUser.observe(this){
            showDetail(it)
        }
        profileViewModel.isLoading.observe(this){
            showLoading(it)
        }

        profileViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = View.GONE
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showDetail(it: DataItem?) {
        binding.tvProfileName.text = it?.name
        binding.tvEmailUser.text = it?.email
        binding.itemProfileDetail.tvUserBirthdate.text = it?.birthdate?.withNewsDateFormat()
        binding.itemProfileDetail.tvUserAge.text = DateHelper.getAge(it?.birthdate!!).toString() + " years old"
        binding.itemProfileDetail.tvUserSex.text = it?.sex?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        binding.itemProfileDetail.tvUserHeight.text = it?.height.toString() + " Cm"
        binding.itemProfileDetail.tvUserWeight.text = it?.weight.toString() + " Kg"

        binding.btnUserUpdate.setOnClickListener {view->
            val intent = Intent(this, UpdateProfileActivity::class.java)
            val updateIntent = ProfileIntent(
                it.name,
                it.birthdate,
                it.sex,
                it.height.toString().toDouble(),
                it.weight.toString().toDouble()
            )
            intent.putExtra("UPDATE_INTENT", updateIntent)
            startActivity(intent)
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
        binding.navBottom.selectedItemId = R.id.bnmProfile
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
                    val intent = Intent(this, ProgressActivity::class.java)
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