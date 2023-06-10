package com.capstone.dietcare.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.dietcare.R
import com.capstone.dietcare.data.helper.ViewModelFactory
import androidx.activity.viewModels
import com.capstone.dietcare.data.remote.news.DataItem
import com.capstone.dietcare.databinding.ActivityMainBinding
import com.capstone.dietcare.favorite.FavoriteActivity
import com.capstone.dietcare.history.HistoryActivity
import com.capstone.dietcare.main.search.SearchActivity
import com.capstone.dietcare.meal.MealActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        bottomNav()
        setupViewModel()
        setupSearch()
        setFavFab()
    }

    private fun setFavFab() {
        binding.favButton.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    private fun setupSearch() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.search
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("SEARCHITEM", query)
                startActivity(intent)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setupViewModel() {
        mainViewModel.getAllNews()
        mainViewModel.getRandomRecipe()
        mainViewModel.listNews.observe(this){
            setNewsAdapter(it)
        }
        mainViewModel.listRandom.observe(this){
            setRandomAdapter(it)
        }
        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        mainViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
    }


    private fun setRandomAdapter(it: List<com.capstone.dietcare.data.remote.ml.DataItem>?) {
        binding.rvRandomRecipe.adapter = RandomRecipeAdapter(it!!)
        binding.rvRandomRecipe.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        binding.rvRandomRecipe.setHasFixedSize(true)
    }

    private fun setNewsAdapter(it: List<DataItem>?) {
        val rvNews = binding.rvNews
        rvNews.adapter = NewsAdapter(it!!)
        rvNews.layoutManager = LinearLayoutManager(this)
        rvNews.setHasFixedSize(true)
        rvNews.isNestedScrollingEnabled = true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun bottomNav(){
        binding.navBottom.selectedItemId = R.id.bnmHome
        binding.navBottom.isItemHorizontalTranslationEnabled = true
        binding.navBottom.setOnNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.bnmHistory -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                }

                R.id.bnmMeal -> {
                    val intent = Intent(this, MealActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    finish()
                }

                R.id.bnmProgress -> {

                }

                R.id.bnmProfile -> {

                }
            }
            true
        }
    }
}