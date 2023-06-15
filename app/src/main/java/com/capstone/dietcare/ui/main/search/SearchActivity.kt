package com.capstone.dietcare.ui.main.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ActivitySearchBinding
import com.capstone.dietcare.ui.favorite.FavoriteActivity
import com.capstone.dietcare.ui.main.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupInitial()
        setupSearch()
        setupViewModel()
        setFavFab()

    }

    private fun setupViewModel() {
        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }
        mainViewModel.isError.observe(this){
            showError(it)
        }

        mainViewModel.snackbarError.observe(this) { error ->
            error.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(window.decorView.rootView, snack, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(5).show()
            }
        }
    }

    private fun setupSearch() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.search
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(this@SearchActivity, SearchActivity::class.java)
                intent.putExtra("SEARCHITEM", query)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setupInitial() {
        val query = intent.getStringExtra("SEARCHITEM")
        if (query!= null){
            binding.search.setQuery(query, false)
            val searchItem = JsonObject()
            searchItem.addProperty("Name", query.replace("(", "\\(").replace(")", "\\)"))
            mainViewModel.postSearch(searchItem)
            mainViewModel.listSearch.observe(this){
                setItemSearch(it)
            }
        }
    }

    private fun setItemSearch(it: List<DataItem>?) {
        binding.rvSearch.adapter = SearchAdapter(it!!)
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.setHasFixedSize(true)
    }

    private fun setFavFab() {
        binding.favButton.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError){
            binding.errorMessage.visibility = View.VISIBLE
        } else {
            binding.errorMessage.visibility = View.GONE
        }
    }
}