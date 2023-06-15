package com.capstone.dietcare.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.R
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.databinding.ActivityRegisterBinding
import com.capstone.dietcare.ui.profile.DatePickerFragment
import com.capstone.dietcare.ui.profile.ProfileActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class RegisterActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var loginViewModel : LoginViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var adapterSex : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setButtonRegis()
        setViewModel()

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setButtonRegis() {
        loginViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        adapterSex = ArrayAdapter(this, R.layout.item_dd, resources.getStringArray(R.array.sex))
        binding.ddRegisSex.setAdapter(adapterSex)

        binding.cvRegisBirthdate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(supportFragmentManager, "DatePicker")
        }

        binding.btnRegis.setOnClickListener {
            when{
                binding.edtRegisName.text.isNullOrEmpty() -> binding.edtRegisName.error = "Name cannot be blank"
                binding.edtRegisEmail.text.isNullOrEmpty() -> binding.edtRegisEmail.error = "Email cannot be blank"
                binding.ddRegisSex.text.isNullOrEmpty() ->binding.ddRegisSex.error = "Enter your biological sex"
                binding.edtRegisHeight.text.isNullOrEmpty() -> binding.edtRegisHeight.error = "Height cannot be blank"
                binding.edtRegisWeigth.text.isNullOrEmpty() -> binding.edtRegisWeigth.error = "Weight cannot be blank"
                binding.edtRegisPassword.text.isNullOrEmpty() -> binding.edtRegisPassword.error = "New password can not be blank"
                binding.edtRegisConfirmPassword.text.isNullOrEmpty() -> binding.edtRegisConfirmPassword.error = "Retype you new password"
                binding.edtRegisPassword.text.toString() != binding.edtRegisConfirmPassword.text.toString() -> binding.edtRegisConfirmPassword.error = "Confirmation password is different with new password"
                binding.datepickerUpdateBirthdate.text.toString() == "Date of Birth" -> Snackbar.make(window.decorView.rootView, "Enter your date of birth", Snackbar.LENGTH_SHORT).show()
                else ->{
                    val newProfile = JsonObject()
                    newProfile.addProperty("name", binding.edtRegisName.text.toString())
                    newProfile.addProperty("password", binding.edtRegisPassword.text.toString())
                    newProfile.addProperty("email", binding.edtRegisEmail.text.toString())
                    newProfile.addProperty("birthdate", binding.datepickerUpdateBirthdate.text.toString())
                    newProfile.addProperty("sex", binding.ddRegisSex.text.toString())
                    newProfile.addProperty("height", binding.edtRegisHeight.text.toString().toDouble())
                    newProfile.addProperty("weight", binding.edtRegisWeigth.text.toString().toDouble())
                    loginViewModel.putRegister(newProfile)
                }
            }
        }




    }

    private fun setViewModel() {

        binding.progressBar.visibility = View.GONE

        loginViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }
        loginViewModel.isError.observe(this){
            showAlert(it)
        }
    }

    private fun showAlert(error : Boolean){
        if (!error){
            AlertDialog.Builder(this).apply {
                setTitle("Register Succeed!")
                setMessage("Welcome to DietCare. Your account has been successfully registered. Please log in with the account you have created.")
                setPositiveButton("Login"){_, _->
                    val loginIntent = Intent(this@RegisterActivity,LoginActivity::class.java)
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(loginIntent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.datepickerUpdateBirthdate.text = dateFormat.format(calendar.time)
    }
}