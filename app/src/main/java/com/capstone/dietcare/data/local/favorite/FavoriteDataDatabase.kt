package com.capstone.dietcare.data.local.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.dietcare.data.local.user.DataUserRoomDatabase

@Database(entities = [FavoriteData::class], version = 1)
abstract class FavoriteDataDatabase: RoomDatabase() {
    abstract fun favoriteDataDao(): FavoriteDataDao

    companion object{
        @Volatile
        private var INSTANCE: FavoriteDataDatabase?= null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteDataDatabase {
            if (INSTANCE == null){
                synchronized(DataUserRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteDataDatabase::class.java, "favoritedata_database")
                        .build()
                }
            }
            return INSTANCE as FavoriteDataDatabase
        }
    }
}