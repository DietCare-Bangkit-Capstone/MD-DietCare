package com.capstone.dietcare.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.dietcare.R
import com.capstone.dietcare.data.local.favorite.FavoriteData
import com.capstone.dietcare.data.helper.ViewModelFactory
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ActivityFavoriteBinding
import com.capstone.dietcare.ui.main.search.SearchAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Favorite Recipes"

        setAdapter()


    }

    private fun setAdapter() {
        favViewModel.getAllFav().observe(this){favData : List<FavoriteData>->
            val favList = arrayListOf<DataItem>()
            favData.map { list->
                val item = DataItem(
                    name = list.name,
                    images = list.img,
                    calories = list.cal,
                    totalTime = list.time,
                    recipeServings = list.port
                )
                favList.add(item)
            }
            binding.rvFavorite.adapter = SearchAdapter(favList)
            binding.rvFavorite.layoutManager = LinearLayoutManager(this)
            binding.rvFavorite.setHasFixedSize(true)

        }
    }
}