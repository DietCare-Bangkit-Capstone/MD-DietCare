package com.capstone.dietcare.data.local.favorite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : FavoriteData)

    @Query("SELECT * from FavoriteData ORDER BY id DESC")
    fun getAllFav(): LiveData<List<FavoriteData>>

    @Query("SELECT * FROM FavoriteData WHERE Name=:name AND Calories=:cal ")
    fun getIsFavorite(name:String, cal: Double): LiveData<FavoriteData>

    @Delete
    fun delete(data: FavoriteData)

}