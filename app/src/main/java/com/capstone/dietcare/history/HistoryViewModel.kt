package com.capstone.dietcare.history

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.user.DataUser
import com.capstone.dietcare.data.user.DataUserRepository

class HistoryViewModel (context: Context):ViewModel(){
    private val mDataUserRepository: DataUserRepository = DataUserRepository(context)

    fun getAllHistory() : LiveData<List<DataUser>> = mDataUserRepository.getAllMeal()
}