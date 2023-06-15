package com.capstone.dietcare.ui.meal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.dietcare.R
import com.capstone.dietcare.data.helper.DateHelper
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.local.user.DataUser
import com.capstone.dietcare.databinding.FragmentModel1Binding
import com.capstone.dietcare.ui.recommendation.RecIntent
import com.capstone.dietcare.ui.recommendation.RecommendationActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import java.util.*

class Model1Fragment : Fragment() {

    private lateinit var binding: FragmentModel1Binding
    private lateinit var adapterDietType : ArrayAdapter<String>
    private lateinit var adapterActivity : ArrayAdapter<String>
    private lateinit var adapterPortion : ArrayAdapter<String>
    private val mealViewModel by viewModels<MealViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentModel1Binding.inflate(inflater, container, false)

        adapterDietType = ArrayAdapter(requireContext(), R.layout.item_dd, resources.getStringArray(R.array.diet_type))
        adapterActivity = ArrayAdapter(requireContext(),R.layout.item_dd, resources.getStringArray(R.array.daily_activity))
        adapterPortion = ArrayAdapter(requireContext(), R.layout.item_dd, resources.getStringArray(R.array.portion))

        binding.ddDietTypeModel1.setAdapter(adapterDietType)
        binding.ddDailyActivityModel1.setAdapter(adapterActivity)
        binding.ddMealModel1.setAdapter(adapterPortion)

        binding.progressBar.visibility = View.GONE
        mealViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }
        mealViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(binding.root, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }

        val user =  UserParcel(
            arguments?.getString("USER_EMAIL").toString(),
            arguments?.getString("USER_PASSWORD").toString()
        )

        binding.btnFindRecipeModel1.setOnClickListener {
            when {
                binding.edtWeight.text.isNullOrEmpty()-> binding.edtWeight.error = "Enter your body weight"
                binding.ddDietTypeModel1.text.isNullOrEmpty() -> binding.ddDietTypeModel1.error = "Enter the level of diet"
                binding.ddDailyActivityModel1.text.isNullOrEmpty() -> binding.ddDailyActivityModel1.error = "Enter your daily activity"
                binding.ddMealModel1.text.isNullOrEmpty() -> binding.ddMealModel1.error = "Enter the number of servings for the day"
                else -> {
                    var dietType : Double = 0.0
                    when {
                        binding.ddDietTypeModel1.text.toString() == "Maintain weight" -> dietType = 0.0
                        binding.ddDietTypeModel1.text.toString() == "Mild weight loss (–0.25 kg/week)" -> dietType = 250.0
                        binding.ddDietTypeModel1.text.toString() == "Weight loss (–0.5 kg/week)" -> dietType = 500.0
                        binding.ddDietTypeModel1.text.toString() == "Extreme weight loss (–1 kg/week)" -> dietType = 1000.0
                        binding.ddDietTypeModel1.text.toString() == "Mild weight gain (+0.25 kg/week)" -> dietType = -250.0
                        binding.ddDietTypeModel1.text.toString() == "Weight gain (+0.5 kg/week)" -> dietType = -500.0
                    }
                    var dailyAct : Double = 0.0
                    when {
                        binding.ddDailyActivityModel1.text.toString() == "Sedentary – little or no exercise" -> dailyAct = 1.2
                        binding.ddDailyActivityModel1.text.toString() == "Light – exercise 1–3 times/week" -> dailyAct = 1.374
                        binding.ddDailyActivityModel1.text.toString() == "Moderate – exercise 4–5 times/week" -> dailyAct = 1.465
                        binding.ddDailyActivityModel1.text.toString() == "Active – daily exercise or intense exercise 3–4 times/week" -> dailyAct = 1.55
                        binding.ddDailyActivityModel1.text.toString() == "Very Active – intense exercise 6–7 times/week" -> dailyAct = 1.725
                        binding.ddDailyActivityModel1.text.toString() == "Extra Active – very intense exercise daily, or physical job" -> dailyAct = 1.9

                    }

                    mealViewModel.getUserDetail(user?.email.toString())

                    mealViewModel.itemUser.observe(requireActivity()){
                        when (it?.sex?.uppercase()){
                            "MALE" -> {
                                var calModel1 : Double = (10.0 * binding.edtWeight.text.toString().toDouble() + 6.25 * it?.height.toString().toDouble() - 5 * DateHelper.getAge(it?.birthdate!!) + 5)*dailyAct - dietType

                                val data = DataUser()
                                data.let {
                                    it.date = DateHelper.getCurrentDate()
                                    it.ms = Date().time
                                    it.meal = binding.ddMealModel1.text.toString().toInt()
                                    it.weight = binding.edtWeight.text.toString().toDouble()
                                    it.calor = calModel1
                                }

                                val profileUpdate = JsonObject()
                                profileUpdate.addProperty("name", it.name)
                                profileUpdate.addProperty("password", user?.password)
                                profileUpdate.addProperty("email", user?.email)
                                profileUpdate.addProperty("birthdate", it.birthdate)
                                profileUpdate.addProperty("sex", it.sex)
                                profileUpdate.addProperty("height", it.height.toString().toDouble())
                                profileUpdate.addProperty("weight", binding.edtWeight.text.toString().toDouble())

                                mealViewModel.putUpdate(profileUpdate)
                                mealViewModel.isError.observe(this){
                                    if (!it){
                                        mealViewModel.insert(data)
                                        val intent = Intent(context, RecommendationActivity::class.java)
                                        startActivity(intent)
                                    }else{
                                        binding.btnFindRecipeModel1.setOnClickListener {
                                            mealViewModel.putUpdate(profileUpdate)
                                        }
                                    }
                                }

                            }
                            "FEMALE" -> {
                                var calModel1 : Double = (10.0 * binding.edtWeight.text.toString().toDouble() + 6.25 * it?.height.toString().toDouble() - 5 * DateHelper.getAge(it.birthdate!!) - 161)*dailyAct - dietType
                                val data = DataUser()
                                data.let {
                                    it.date = DateHelper.getCurrentDate()
                                    it.ms = Date().time
                                    it.meal = binding.ddMealModel1.text.toString().toInt()
                                    it.weight = binding.edtWeight.text.toString().toDouble()
                                    it.calor = calModel1
                                }

                                val profileUpdate = JsonObject()
                                profileUpdate.addProperty("name", it.name)
                                profileUpdate.addProperty("password", user?.password)
                                profileUpdate.addProperty("email", user?.email)
                                profileUpdate.addProperty("birthdate", it.birthdate)
                                profileUpdate.addProperty("sex", it.sex)
                                profileUpdate.addProperty("height", it.height.toString().toDouble())
                                profileUpdate.addProperty("weight", binding.edtWeight.text.toString().toDouble())

                                mealViewModel.putUpdate(profileUpdate)
                                mealViewModel.isError.observe(this){
                                    if (!it){
                                        mealViewModel.insert(data)
                                        val intent = Intent(context, RecommendationActivity::class.java)
                                        startActivity(intent)
                                    }else{
                                        binding.btnFindRecipeModel1.setOnClickListener {
                                            mealViewModel.putUpdate(profileUpdate)
                                        }
                                    }
                                }
                            }
                            null -> {
                                mealViewModel.getUserDetail(user?.email.toString())
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}