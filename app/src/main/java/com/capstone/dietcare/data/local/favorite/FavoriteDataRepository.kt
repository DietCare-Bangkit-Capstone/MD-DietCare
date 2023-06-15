package com.capstone.dietcare.data.local.favorite

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteDataRepository(context: Context) {
    private val mFavoriteDataDao: FavoriteDataDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDataDatabase.getDatabase(context)
        mFavoriteDataDao = db.favoriteDataDao()
    }

    fun insert(data : FavoriteData){
        executorService.execute { mFavoriteDataDao.insert(data) }
    }

    fun getAllFav(): LiveData<List<FavoriteData>> = mFavoriteDataDao.getAllFav()

    fun getIsFavorite(name : String, cal: Double): LiveData<FavoriteData> = mFavoriteDataDao.getIsFavorite(name, cal)

    fun delete(data: FavoriteData){
        executorService.execute { mFavoriteDataDao.delete(data) }
    }
}