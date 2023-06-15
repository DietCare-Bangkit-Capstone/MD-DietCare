package com.capstone.dietcare.ui.meal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.capstone.dietcare.R
import com.capstone.dietcare.data.helper.DateHelper
import com.capstone.dietcare.data.local.user.DataUser
import com.capstone.dietcare.databinding.FragmentModel2Binding
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.ui.recommendation.RecommendationActivity
import java.util.*


class Model2Fragment : Fragment() {

    private val mealViewModel by viewModels<MealViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var binding: FragmentModel2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentModel2Binding.inflate(inflater, container, false)
        binding.ddMealModel2.setAdapter(ArrayAdapter(requireContext(), R.layout.item_dd, resources.getStringArray(R.array.portion)))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFindRecipeModel2.setOnClickListener {
            when {
                binding.ddMealModel2.text.toString().isNullOrEmpty() -> binding.ddMealModel2.error = "Enter the number of servings for the day"
                else -> {
                    val data = DataUser()
                    data.let {
                        it.date = DateHelper.getCurrentDate()
                        it.ms = Date().time
                        it.meal = binding.ddMealModel2.text.toString().toInt()
                        it.calor = binding.sldCaloriesModel2.value.toDouble()
                        it.fat = binding.sldFatModel2.value.toDouble()
                        it.satfat = binding.sldSaturatedFatModel2.value.toDouble()
                        it.chol = binding.sldCholesterolModel2.value.toDouble()
                        it.sodium = binding.sldSodiumModel2.value.toDouble()
                        it.carb = binding.sldCarbohydrateModel2.value.toDouble()
                        it.fiber = binding.sldFiberModel2.value.toDouble()
                        it.sugar = binding.sldSugarModel2.value.toDouble()
                        it.protein = binding.sldProteinModel2.value.toDouble()
                    }
                    mealViewModel.insert(data)

                    val intent = Intent(context, RecommendationActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}