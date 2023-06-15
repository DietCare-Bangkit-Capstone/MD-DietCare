package com.capstone.dietcare.data.local.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DataUser::class], version = 1)
abstract class DataUserRoomDatabase: RoomDatabase() {
    abstract fun dataUserDao() : DataUserDao

    companion object{
        @Volatile
        private var INSTANCE: DataUserRoomDatabase?= null

        @JvmStatic
        fun getDatabase(context: Context): DataUserRoomDatabase {
            if (INSTANCE == null){
                synchronized(DataUserRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    DataUserRoomDatabase::class.java, "datauser_database")
                        .build()
                }
            }
            return INSTANCE as DataUserRoomDatabase
        }
    }

}