package com.capstone.dietcare.ui.progress

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.helper.Event
import com.capstone.dietcare.data.remote.profile.DataItem
import com.capstone.dietcare.data.remote.profile.ProfileConfig
import com.capstone.dietcare.data.remote.profile.SingleUserResponseItem
import com.capstone.dietcare.data.local.user.DataUser
import com.capstone.dietcare.data.local.user.DataUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressViewModel (context: Context): ViewModel() {

    private val mDataUserRepository: DataUserRepository = DataUserRepository(context)

    fun getAllBW() : LiveData<List<DataUser>> = mDataUserRepository.getAllBodyWeight()

    fun getLatestBW() : LiveData<DataUser> = mDataUserRepository.getLatestBodyWeight()

    fun getAllMeall() : LiveData<List<DataUser>> = mDataUserRepository.getAllCalData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    private val _itemUser = MutableLiveData<DataItem?>()
    val itemUser: MutableLiveData<DataItem?> = _itemUser

    fun getUserDetail(email: String){
        _isLoading.value = true
        val client = ProfileConfig.getApiService().getSingleUser(email)
        client.enqueue(object : Callback<List<SingleUserResponseItem?>?> {
            override fun onResponse(
                call: Call<List<SingleUserResponseItem?>?>,
                response: Response<List<SingleUserResponseItem?>?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.get(0)?.data?.get(0)?.email != null) {
                        _itemUser.value = response.body()!!.get(0)?.data?.get(0)
                    }else{
                        Log.e("ProgressViewModel", "onFailure: data index 0 = null")
                        _snackbarError.value = Event("onFailure: data index 0 = null")
                    }
                }else{
                    Log.e("ProgressViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<SingleUserResponseItem?>?>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProgressViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }
}