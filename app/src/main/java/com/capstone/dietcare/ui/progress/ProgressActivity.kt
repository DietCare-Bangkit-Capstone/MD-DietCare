package com.capstone.dietcare.ui.progress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.R
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.local.login.LoginViewModelFactory
import com.capstone.dietcare.data.local.login.PreferenceViewModel
import com.capstone.dietcare.data.local.user.DataUser
import com.capstone.dietcare.databinding.ActivityProgressBinding
import com.capstone.dietcare.ui.history.HistoryActivity
import com.capstone.dietcare.ui.main.MainActivity
import com.capstone.dietcare.ui.meal.MealActivity
import com.capstone.dietcare.ui.profile.ProfileActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProgressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgressBinding
    private val progressViewModel by viewModels<ProgressViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var prefViewModel : PreferenceViewModel
    private lateinit var weightChart: LineChart
    private lateinit var calorChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Your Diet Progress"

        setViewModel()
        bottomNav()
    }

    private fun setViewModel() {
        prefViewModel = ViewModelProvider(this,
            LoginViewModelFactory(LoginPreference.getInstance(dataStore))
        )[PreferenceViewModel::class.java]
        progressViewModel.getLatestBW().observe(this){
            if (it != null){
                setLatestData(it)
            }
        }
        progressViewModel.getAllBW().observe(this){
            if (it.size > 0){
                setProgressChart(it)
            }
        }
        progressViewModel.getAllMeall().observe(this){
            if (it.size > 0){
                setCaloriesChart(it)
            }
        }
        prefViewModel.isLogin().observe(this){
            progressViewModel.getUserDetail(it.email)
        }
        progressViewModel.isLoading.observe(this){
            showLoading(it)
        }
        progressViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
    }

    private fun setCaloriesChart(data: List<DataUser>?) {
        val dataCalories = arrayListOf<Entry>()
        data?.map {
            val item = Entry(
                it.ms!!.toFloat(),
                it.calor!!.toFloat()
            )
            dataCalories.add(item)
        }

        var calorDataset = LineDataSet(dataCalories, "Calories Input Data")
        var calorData = LineData(calorDataset)
        calorChart = binding.lcCaloriesChart
        calorChart.data = calorData
        calorChart.invalidate()
        calorDataset.color = resources.getColor(R.color.bmi_obese2)
        calorDataset.setCircleColors(resources.getColor(R.color.bmi_obese2))
        calorChart.legend.isEnabled = false
        calorChart.xAxis.valueFormatter = XValueDateFormatter()
        calorChart.xAxis.labelRotationAngle = 90.0F
        calorChart.extraTopOffset = 45.0F
        calorChart.description.text = "X = Date; Y = Calories (kcal)"

    }

    private fun setProgressChart(data: List<DataUser>?) {
        val dataWeight = arrayListOf<Entry>()
        data?.map {point->
            val item = Entry(
                point.ms!!.toFloat(),
                point.weight!!.toFloat()
            )
            dataWeight.add(item)
        }


        binding.tvWeighttoLastDiff.visibility = View.GONE
        binding.tvWeighttoFirstDiff.visibility = View.GONE

        when{
            data?.size!! == 1 ->{
                val firstWeight = data?.get(0)?.weight
                val lastWeight = data?.size?.let { data.get(it.minus(1)).weight }
                binding.tvWeighttoFirstDiff.visibility = View.VISIBLE
                if (lastWeight != null && firstWeight != null){
                    if (lastWeight!! > firstWeight!!){
                        binding.tvWeighttoFirstDiff.text = "Gained ${BigDecimal(lastWeight.minus(firstWeight)).setScale(2, RoundingMode.HALF_UP)} kg from your first progress"
                    }else{
                        binding.tvWeighttoFirstDiff.text = "Lost ${BigDecimal(firstWeight.minus(lastWeight)).setScale(2, RoundingMode.HALF_UP)} kg from your first progress"
                    }
                }
            }
            data?.size!! > 1 ->{

                val firstWeight = data?.get(0)?.weight
                val lastWeight = data?.size?.let { data.get(it.minus(1)).weight }
                binding.tvWeighttoLastDiff.visibility = View.VISIBLE
                binding.tvWeighttoFirstDiff.visibility = View.VISIBLE
                if (lastWeight != null && firstWeight != null){
                    if (lastWeight!! > firstWeight!!){
                        binding.tvWeighttoFirstDiff.text = "Gained ${BigDecimal(lastWeight.minus(firstWeight)).setScale(2, RoundingMode.HALF_UP)} kg from your first progress"
                    }else{
                        binding.tvWeighttoFirstDiff.text = "Lost ${BigDecimal(firstWeight.minus(lastWeight)).setScale(2, RoundingMode.HALF_UP)} kg from your first progress"
                    }
                }

                val secLastWeight = data?.get(data.size.minus(2))?.weight
                Log.d("bedabanget", "${secLastWeight}")
                if (lastWeight != null && secLastWeight != null){
                    if (lastWeight!! > secLastWeight!!){
                        binding.tvWeighttoLastDiff.text = "Gained ${BigDecimal(lastWeight.minus(secLastWeight)).setScale(2, RoundingMode.HALF_UP)} kg from your last data"
                    }else{
                        binding.tvWeighttoLastDiff.text = "Lost ${BigDecimal(secLastWeight.minus(lastWeight)).setScale(2, RoundingMode.HALF_UP)} kg from your last data"
                    }
                }
            }

        }


        var dataSet = LineDataSet(dataWeight, "Body Weight Data")
        var lineData = LineData(dataSet)
        weightChart = binding.lcProgressChart
        weightChart.data = lineData
        weightChart.invalidate()
        weightChart.xAxis.valueFormatter = XValueDateFormatter()
        weightChart.xAxis.labelRotationAngle = 90.0F
        weightChart.extraTopOffset = 45.0F
        weightChart.description.text = "X = Date; Y = Body Weight (kg)"

    }


    private fun setLatestData(data: DataUser?) {
        binding.tvDateWeight.text = "Recent body weight data: ${data?.date}"

        progressViewModel.itemUser.observe(this){
            val userHeight : Double = it?.height.toString().toDouble()/100

            val userWeight = data?.weight

            val userBmi = userWeight?.div(userHeight*userHeight)

            val userWeightMin = 18.5 * userHeight * userHeight
            val userWeightMax = 25 * userHeight * userHeight

            binding.tvRangeBmiUser.text = "Healthy weight to height: ${BigDecimal(userWeightMin.toString()).setScale(2, RoundingMode.HALF_UP)} kg - ${BigDecimal(userWeightMax.toString()).setScale(2, RoundingMode.HALF_UP)} kg"

            if (userWeight != null) {
                when{
                    userWeight < userWeightMin -> binding.tvBmitoIdeal.text = "Gain ${BigDecimal(userWeightMin.minus(userWeight).toString()).setScale(2, RoundingMode.HALF_UP)} kg more to reach the BMI ideal of 18.5 kg/m²"
                    userWeight > userWeightMax -> binding.tvBmitoIdeal.text = "Lose ${BigDecimal(userWeight.minus(userWeightMax).toString()).setScale(2, RoundingMode.HALF_UP)} kg more to reach the BMI ideal of 25 kg/m²"
                    else -> binding.tvBmitoIdeal.text = "You're on Ideal BMI, keep it up!"
                }
            }

            binding.tvBmiUser.text = "${BigDecimal(userBmi.toString()).setScale(2, RoundingMode.HALF_UP)} kg/m²"
            when{
                userBmi!! in 0.0 .. 16.0 -> {

                    binding.tvBmiType.text = "Severe Thinness"
                    binding.tvBmiType.setTextColor(resources.getColor(R.color.white))
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_severe_thinness))
                }
                userBmi in 16.0 .. 17.0 -> {
                    binding.tvBmiType.text = "Moderate Thinness"
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_moderate_thinness))
                }
                userBmi in 17.0 .. 18.5 -> {
                    binding.tvBmiType.text = "Mild Thinness"
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_mild_thinness))
                }
                userBmi in 18.5 .. 25.0 -> {
                    binding.tvBmiType.text = "Normal"
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_normal))
                }
                userBmi in 25.0 .. 30.0 -> {
                    binding.tvBmiType.text = "Overweight"
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_overweight))
                }
                userBmi in 30.0 .. 35.0 -> {
                    binding.tvBmiType.text = "Obese Class I"
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_obese1))
                }
                userBmi in 35.0 .. 40.0 -> {
                    binding.tvBmiType.text = "Obese Class II"
                    binding.tvBmiType.setTextColor(resources.getColor(R.color.white))
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_obese2))
                }
                userBmi > 40.0 -> {
                    binding.tvBmiType.text = "Obese Class III"
                    binding.tvBmiType.setTextColor(resources.getColor(R.color.white))
                    binding.cvBMIUser.setCardBackgroundColor(resources.getColor(R.color.bmi_obese3))
                }
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

    private fun bottomNav(){
        binding.bnmMeal.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()
        }
        binding.navBottom.selectedItemId = R.id.bnmProgress
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

class XValueDateFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val timeInMs  = Date(value.toLong())
        val timeFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US)
        return timeFormat.format(timeInMs)
    }
}
