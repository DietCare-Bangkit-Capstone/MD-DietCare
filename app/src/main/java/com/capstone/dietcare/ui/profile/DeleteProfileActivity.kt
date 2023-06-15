package com.capstone.dietcare.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.ui.auth.LoginActivity
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.data.local.login.PreferenceViewModel
import com.capstone.dietcare.databinding.ActivityDeleteProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DeleteProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var prefViewModel : PreferenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Delete Account"

        setViewModel()
        deleteUser()
    }

    private fun deleteUser() {
        prefViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[PreferenceViewModel::class.java]

        binding.btnDeleteAccount.setOnClickListener {
            when {
                binding.edtDeleteEmail.text.isNullOrEmpty() -> binding.edtDeleteEmail.error = "Email can not be blank"
                binding.edtDeletePassword.text.isNullOrEmpty() -> binding.edtDeletePassword.error = "Password can not be blank"
                else -> {
                    prefViewModel.isLogin().observe(this){login->
                        when {
                            binding.edtDeleteEmail.text.toString() != login.email -> binding.edtDeleteEmail.error = "Email is wrong"
                            binding.edtDeletePassword.text.toString() != login.password -> binding.edtDeletePassword.error = "Password is wrong"
                            else ->{
                                val account = JsonObject()
                                account.addProperty("email", login.email)
                                profileViewModel.deleteUser(account)
                                profileViewModel.isError.observe(this){error->
                                    if (!error){
                                        prefViewModel.logout()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        binding.btnDeleteAccount.setOnClickListener {
                                            profileViewModel.deleteUser(account)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setViewModel() {
        binding.progressBar.visibility = View.GONE
        profileViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
        profileViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}