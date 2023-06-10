package com.capstone.dietcare.data.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.detail.DetailViewModel
import com.capstone.dietcare.favorite.FavoriteViewModel
import com.capstone.dietcare.history.HistoryViewModel
import com.capstone.dietcare.main.MainViewModel
import com.capstone.dietcare.meal.MealViewModel
import com.capstone.dietcare.recommendation.RecViewModel

class ViewModelFactory private constructor( private val context: Context) : ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance (context: Context) : ViewModelFactory {
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(context)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(MainViewModel::class.java) ->{
                return MainViewModel(context) as T }
            modelClass.isAssignableFrom(RecViewModel::class.java) ->{
                return RecViewModel(context) as T }
            modelClass.isAssignableFrom(MealViewModel::class.java) ->{
                return MealViewModel(context) as T }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->{
                return HistoryViewModel(context) as T }
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->{
                return DetailViewModel(context) as T }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) ->{
                return FavoriteViewModel(context) as T }
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}