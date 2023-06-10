package com.capstone.dietcare.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.favorite.FavoriteData
import com.capstone.dietcare.data.favorite.FavoriteDataRepository

class FavoriteViewModel(context: Context): ViewModel() {
    private val  mFavoriteDataRepository: FavoriteDataRepository = FavoriteDataRepository(context)

    fun getAllFav():LiveData<List<FavoriteData>> = mFavoriteDataRepository.getAllFav()
}