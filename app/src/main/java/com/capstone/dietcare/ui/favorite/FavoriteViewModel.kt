package com.capstone.dietcare.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.local.favorite.FavoriteData
import com.capstone.dietcare.data.local.favorite.FavoriteDataRepository

class FavoriteViewModel(context: Context): ViewModel() {
    private val  mFavoriteDataRepository: FavoriteDataRepository = FavoriteDataRepository(context)

    fun getAllFav():LiveData<List<FavoriteData>> = mFavoriteDataRepository.getAllFav()
}