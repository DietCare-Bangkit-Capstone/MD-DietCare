package com.capstone.dietcare.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.databinding.ActivityLoginBinding
import com.capstone.dietcare.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        setupViewModel()
        setupLogin()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.snackbarError.observe(this){ error ->
            error.getContentIfNotHandled()?.let {snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT).setTextMaxLines(5).show()
            }
        }
        binding.progressBar.visibility = View.GONE
        loginViewModel.isLoading.observe(this){showLoading(it)}
        loginViewModel.isError.observe(this){successLogin(it)}
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupLogin() {
        binding.btnLogin.setOnClickListener{
            when {
                binding.edtLoginEmail.text.toString().isEmpty() -> binding.edtLoginEmail.error = "Email can not be blank"
                binding.edtLoginPassword.text.toString().isEmpty() -> binding.edtLoginPassword.error = "Password can not be blank"
                else -> {
                    val email = binding.edtLoginEmail.text.toString()
                    val password = binding.edtLoginPassword.text.toString()

                    loginViewModel.login(email, password)
                }
            }
        }

    }

    private fun successLogin(isError : Boolean) {
        if (!isError){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}