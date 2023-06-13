package com.capstone.dietcare.progress

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.user.DataUser
import com.capstone.dietcare.data.user.DataUserRepository

class ProgressViewModel (context: Context): ViewModel() {

    private val mDataUserRepository: DataUserRepository = DataUserRepository(context)

    fun getAllBW() : LiveData<List<DataUser>> = mDataUserRepository.getAllBodyWeight()

    fun getLatestBW() : LiveData<DataUser> = mDataUserRepository.getLatestBodyWeight()

    fun getAllMeall() : LiveData<List<DataUser>> = mDataUserRepository.getAllCalData()
}