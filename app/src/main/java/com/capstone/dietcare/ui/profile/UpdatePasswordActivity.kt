package com.capstone.dietcare.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.data.local.login.PreferenceViewModel
import com.capstone.dietcare.databinding.ActivityUpdatePasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var prefViewModel : PreferenceViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Update Password"

        setViewModel()
        setUpdatePassword()
    }

    private fun setUpdatePassword() {
        prefViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[PreferenceViewModel::class.java]

        binding.btnUpdatePassword.setOnClickListener {
            when{
                binding.edtOldPassword.text.isNullOrEmpty() -> binding.edtOldPassword.error = "Old password can not be blank"
                binding.edtNewPassword.text.isNullOrEmpty() -> binding.edtNewPassword.error = "New password can not be blank"
                binding.edtConfirmNewPassword.text.isNullOrEmpty() -> binding.edtConfirmNewPassword.error = "Retype you new password"
                binding.edtNewPassword.text.toString() != binding.edtConfirmNewPassword.text.toString() -> binding.edtConfirmNewPassword.error = "Confirmation password is different with new password"
                else -> {
                    prefViewModel.isLogin().observe(this){login->
                        if(binding.edtOldPassword.text.toString() != login.password){
                            binding.edtOldPassword.error = "Old password is wrong"
                        }else{
                            profileViewModel.getUserDetail(login.email)
                            profileViewModel.itemUser.observe(this){
                                val profileUpdate = JsonObject()
                                profileUpdate.addProperty("name", it?.name)
                                profileUpdate.addProperty("password", binding.edtNewPassword.text.toString())
                                profileUpdate.addProperty("email", login.email)
                                profileUpdate.addProperty("birthdate", it?.birthdate)
                                profileUpdate.addProperty("sex", it?.sex)
                                profileUpdate.addProperty("height", it?.height.toString().toDouble())
                                profileUpdate.addProperty("weight", it?.weight.toString().toDouble())
                                profileViewModel.putUpdate(profileUpdate, login.email)
                                profileViewModel.isError.observe(this){
                                    if (!it){
                                        prefViewModel.prefUpdate(login.email, binding.edtNewPassword.text.toString())
                                        val intent = Intent(this, ProfileActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        binding.btnUpdatePassword.setOnClickListener {
                                            profileViewModel.putUpdate(profileUpdate,login.email)
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