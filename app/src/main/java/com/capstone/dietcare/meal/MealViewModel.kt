package com.capstone.dietcare.meal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.user.DataUser
import com.capstone.dietcare.data.user.DataUserRepository

class MealViewModel (context: Context): ViewModel() {
    private val mDataUserRepository: DataUserRepository = DataUserRepository(context)

    fun insert (data: DataUser){
        mDataUserRepository.insert(data)
    }


}