package com.capstone.dietcare.data.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.dietcare.ui.detail.DetailViewModel
import com.capstone.dietcare.ui.favorite.FavoriteViewModel
import com.capstone.dietcare.ui.history.HistoryViewModel
import com.capstone.dietcare.ui.main.MainViewModel
import com.capstone.dietcare.ui.meal.MealViewModel
import com.capstone.dietcare.ui.profile.ProfileViewModel
import com.capstone.dietcare.ui.progress.ProgressViewModel
import com.capstone.dietcare.ui.recommendation.RecViewModel

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
            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->{
                return ProgressViewModel(context) as T }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->{
                return ProfileViewModel(context) as T }
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}