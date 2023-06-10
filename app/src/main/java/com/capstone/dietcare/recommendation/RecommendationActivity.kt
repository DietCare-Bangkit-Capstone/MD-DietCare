package com.capstone.dietcare.recommendation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ActivityRecommendationBinding
import com.capstone.dietcare.history.HistoryActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private val recViewModel by viewModels<RecViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Choose Your Breakfast"

        setViewModel()
        getRecommendation()
        getRecIntent()
    }

    private fun getRecIntent() {
        val recom = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("RECINTENT", RecIntent::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("RECINTENT")
        }

        if(recom !=null){
            recViewModel.updateMeal5(recom)
            val intent = Intent(this, HistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()
        }
    }

    private fun setViewModel(){
        recViewModel.listRecommendation.observe(this){
            setPager(it)
        }
        recViewModel.isLoading.observe(this){
            showLoading(it)
        }
        recViewModel.isError.observe(this){
            showRefresh(it)
        }
        recViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
    }

    private fun getRecommendation(){
        recViewModel.getLatestMeal().observe(this){ nutrition->
            if (nutrition != null){
                binding.tvCalLeft.text = "Your calorie needs : ${nutrition.calor?.toInt()} kcal"
                if (nutrition.fat != null){
                    val nutri = JsonObject()
                    nutri.addProperty("Calories",nutrition.calor!!.div(nutrition.meal!!.toDouble()) )
                    nutri.addProperty("FatContent", nutrition.fat!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("SaturatedFatContent", nutrition.satfat!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("CholesterolContent", nutrition.chol!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("SodiumContent", nutrition.sodium!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("CarbohydrateContent", nutrition.carb!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("FiberContent", nutrition.fiber!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("SugarContent", nutrition.sugar!!.div(nutrition.meal!!.toDouble()))
                    nutri.addProperty("ProteinContent", nutrition.protein!!.div(nutrition.meal!!.toDouble()))

                    recViewModel.getModel2(nutri)

                } else {
                    val nutri1 = JsonObject()
                    nutri1.addProperty("Calories",nutrition.calor!!.div(nutrition.meal!!.toDouble()))

                    recViewModel.getModel1(nutri1)

                }
            } else{
                Toast.makeText(this, "Maaf, terdapat kesalahan teknis.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRefresh(isError: Boolean) {
        if (isError){
            binding.btnRefresh.visibility = View.VISIBLE
            binding.tvRefresh.visibility = View.VISIBLE
        } else {
            binding.btnRefresh.visibility = View.GONE
            binding.tvRefresh.visibility = View.GONE
        }
        binding.btnRefresh.setOnClickListener {
            getRecommendation()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setPager(it: List<DataItem>?) {
        val pager = binding.pagerMeal
        pager.adapter = RecPagerAdapter(it!!)
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicatorRecommendation.setViewPager(pager)
    }

}