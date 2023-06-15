package com.capstone.dietcare.ui.recommendation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ActivityRecommendation2Binding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

class Recommendation2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendation2Binding
    private val recViewModel by viewModels<RecViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendation2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Choose Your Lunch"

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
            recViewModel.updateMeal1(recom)
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

                if(nutrition.m1cal != null){
                    binding.tvCalLeft.text = "Remaining calorie needs : ${nutrition.calor?.toInt()?.minus(nutrition.m1cal!!.toInt())} kcal"
                }

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
                Toast.makeText(this, "Sorry, an rror occurred. Please do a refresh.", Toast.LENGTH_SHORT).show()
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
        pager.adapter = Rec2PagerAdapter(it!!)
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicatorRecommendation.setViewPager(pager)
    }

}