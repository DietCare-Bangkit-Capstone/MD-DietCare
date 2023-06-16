package com.capstone.dietcare.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.R
import com.capstone.dietcare.data.helper.DateHelper
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.data.local.login.PreferenceViewModel
import com.capstone.dietcare.databinding.ActivityUpdateProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class UpdateProfileActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityUpdateProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var prefViewModel : PreferenceViewModel
    private lateinit var adapterSex : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Update Profile"

        setViewModel()
        setInitial()
        setButton()
    }

    private fun setButton() {

        prefViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[PreferenceViewModel::class.java]

        binding.cvUpdateBirthdate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(supportFragmentManager, "DatePicker")
        }

        binding.btnUpdateUser.setOnClickListener {
            when{
                binding.edtUpdateName.text.isNullOrEmpty() -> binding.edtUpdateName.error = "Name cannot be blank"
                binding.ddUpdateSex.text.isNullOrEmpty() ->binding.ddUpdateSex.error = "Enter your biological sex"
                binding.edtUpdateHeight.text.isNullOrEmpty() -> binding.edtUpdateHeight.error = "Height cannot be blank"
                binding.edtUpdateWeight.text.isNullOrEmpty() -> binding.edtUpdateWeight.error = "Weight cannot be blank"
                else ->{
                    prefViewModel.isLogin().observe(this){login->
                        val profileUpdate = JsonObject()
                        profileUpdate.addProperty("name", binding.edtUpdateName.text.toString())
                        profileUpdate.addProperty("password", login.password)
                        profileUpdate.addProperty("email", login.email)
                        profileUpdate.addProperty("birthdate", binding.datepickerUpdateBirthdate.text.toString())
                        profileUpdate.addProperty("sex", binding.ddUpdateSex.text.toString())
                        profileUpdate.addProperty("height", binding.edtUpdateHeight.text.toString().toDouble())
                        profileUpdate.addProperty("weight", binding.edtUpdateWeight.text.toString().toDouble())
                        profileViewModel.putUpdate(profileUpdate, login.email)
                        profileViewModel.isError.observe(this){
                            if (!it){
                                profileViewModel.updateBodyWeight(binding.edtUpdateWeight.text.toString().toDouble(), Date().time, DateHelper.getCurrentDate())
                                val intent = Intent(this, ProfileActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }else{
                                binding.btnUpdateUser.setOnClickListener {
                                    profileViewModel.putUpdate(profileUpdate,login.email)
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.datepickerUpdateBirthdate.text = dateFormat.format(calendar.time)
    }

    private fun setInitial() {
        val dataInit = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("UPDATE_INTENT", ProfileIntent::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("UPDATE_INTENT")
        }

        binding.progressBar.visibility = View.GONE

        adapterSex = ArrayAdapter(this, R.layout.item_dd, resources.getStringArray(R.array.sex))
        binding.ddUpdateSex.setAdapter(adapterSex)

        if (dataInit != null){
            binding.edtUpdateName.setText(dataInit.name)
            binding.datepickerUpdateBirthdate.setText(dataInit.date)
            binding.edtUpdateHeight.setText(dataInit.height.toString())
            binding.edtUpdateWeight.setText(dataInit.weight.toString())

        }
    }

    private fun setViewModel() {
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