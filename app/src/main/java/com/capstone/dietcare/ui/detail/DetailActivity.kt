package com.capstone.dietcare.ui.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.dietcare.R
import com.capstone.dietcare.data.local.favorite.FavoriteData
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ActivityDetailBinding
import com.capstone.dietcare.ui.favorite.FavoriteActivity
import com.capstone.dietcare.ui.recommendation.RecIntent
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupInitial()
        setViewModel()

    }

    private fun setViewModel() {
        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detailViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
    }

    private fun setupInitial() {
        val recipe = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("RECIPE_INTENT", DetailParcel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("RECIPE_INTENT")
        }
        if (recipe != null){
            val name = JsonObject()
            name.addProperty("Name", recipe.name.replace("(", "\\(").replace(")", "\\)"))
            detailViewModel.postDetail(name, recipe.name, recipe.cal)
            detailViewModel.itemSearch.observe(this){
                setDetail(it)
            }
        }
        binding.favButton.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    private fun setDetail(it: DataItem?) {
        val image = it?.images?.removePrefix("\"")?.removeSuffix("\"")?.replaceAfter(".jpg", "" )?.replaceAfter(".JPG", "" )?.replaceAfter(".jpeg", "" )?.replaceAfter(".JPEG", "" )?.replaceAfter(".png", "" )?.replaceAfter(".PNG", "" )

        binding.tvItemRecipeTitle.text = it?.name
        binding.tvCPFItemRecipe.text = "Carbohydrate: ${it?.carbohydrateContent}g • Protein: ${it?.proteinContent}g • Fat: ${it?.fatContent}g"
        Glide.with(binding.ivItemRecipe).load(image).into(binding.ivItemRecipe)

        binding.tvCaloriesItemRecipe.text = "${it?.calories} kcal"
        binding.tvTimeItemRecipe.text = it?.totalTime?.removePrefix("PT")?.replace("H", " hr ")?.replace("M", " min")
        binding.tvPortionItemRecipe.text = it?.recipeServings.toString().removeSuffix(".0") + " portion"

        val ingredientArray = it?.recipeIngredientParts?.removePrefix("\"")?.removeSuffix("\"")?.split("\", \"")?.toTypedArray()
        if (ingredientArray != null) {
            var tidyIngridient = ""
            for (part : String in ingredientArray){
                tidyIngridient = tidyIngridient + "• ${part}\n"
            }
            binding.tvIngridientsItemRecipe.text = tidyIngridient
        }

        val stepArray = it?.recipeInstructions?.removeSuffix(")")?.removePrefix("\"")?.removeSuffix("\"")?.removeSuffix("\"\n")?.replace("\", \n\"", "\", \"")?.split("\", \"")?.toTypedArray()
        if (stepArray != null){
            var tidyStep = ""
            for (i in 1..stepArray.size){
                tidyStep = tidyStep + "${i}. ${stepArray[i-1]}\n"
            }
            binding.tvStepsItemRecipe.text = tidyStep
        }

        binding.tvNutritionItemRecipe.tvDetailCalories.text = "${it?.calories} kilocalories"
        binding.tvNutritionItemRecipe.tvDetailFat.text = "${it?.fatContent} gram"
        binding.tvNutritionItemRecipe.tvDetailSaturFat.text = "${it?.saturatedFatContent} gram"
        binding.tvNutritionItemRecipe.tvDetailCholesterol.text = "${it?.cholesterolContent} gram"
        binding.tvNutritionItemRecipe.tvDetailSodium.text = "${it?.sodiumContent} gram"
        binding.tvNutritionItemRecipe.tvDetailCarbo.text = "${it?.carbohydrateContent} gram"
        binding.tvNutritionItemRecipe.tvDetailFiber.text = "${it?.fiberContent} gram"
        binding.tvNutritionItemRecipe.tvDetailSugar.text = "${it?.sugarContent} gram"
        binding.tvNutritionItemRecipe.tvDetailProtein.text = "${it?.proteinContent} gram"

        setFavorite(it)
    }

    private fun setFavorite(intent: DataItem?) {
        var isFavorite = false
        val favData = FavoriteData()
        favData.let {
            it.name = intent?.name
            it.cal = intent?.calories.toString().toDouble()
            it.img = intent?.images
            it.time = intent?.totalTime?.removePrefix("PT")
            it.port = intent?.recipeServings.toString().toDouble()
        }
        if (intent?.name != null && intent.calories !=null){
            detailViewModel.isFavorite(intent.name!!, intent.calories.toString().toDouble()).observe(this){ data->
                if(data != null){
                    isFavorite = !isFavorite
                    binding.btnFavoriteDetail.setBackgroundColor(resources.getColor(R.color.green_700))
                    binding.btnFavoriteDetail.text = "Favorited"
                }else{
                    binding.btnFavoriteDetail.setBackgroundColor(resources.getColor(R.color.green_200))
                    binding.btnFavoriteDetail.text = "Mark As Favorite"
                }

                binding.btnFavoriteDetail.setOnClickListener {
                    if (isFavorite){
                        favData.let {
                            it.id = data.id
                        }
                        detailViewModel.deleteFav(favData)
                        isFavorite = !isFavorite
                    }else{
                        detailViewModel.insertFav(favData)
                    }
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
}