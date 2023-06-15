package com.capstone.dietcare.ui.meal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.helper.Event
import com.capstone.dietcare.data.remote.profile.DataItem
import com.capstone.dietcare.data.remote.profile.ProfileConfig
import com.capstone.dietcare.data.remote.profile.SingleUserResponseItem
import com.capstone.dietcare.data.remote.profile.UpdateUserResponseItem
import com.capstone.dietcare.data.local.user.DataUser
import com.capstone.dietcare.data.local.user.DataUserRepository
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel (context: Context): ViewModel() {
    private val mDataUserRepository: DataUserRepository = DataUserRepository(context)

    fun insert (data: DataUser){
        mDataUserRepository.insert(data)
    }


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

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
                        Log.e("MealViewModel", "onFailure: data index 0 = null")
                        _snackbarError.value = Event("onFailure: data index 0 = null")
                    }
                }else{
                    Log.e("MealViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<SingleUserResponseItem?>?>, t: Throwable) {
                _isLoading.value = false
                Log.e("MealViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }

    fun putUpdate(body: JsonObject){
        _isLoading.value = true
        val client = ProfileConfig.getApiService().updateUser("ardanar.rd@gmail.com", body)
        client.enqueue(object : Callback<List<UpdateUserResponseItem?>?> {
            override fun onResponse(
                call: Call<List<UpdateUserResponseItem?>?>,
                response: Response<List<UpdateUserResponseItem?>?>
            ) {
                _isLoading.value = false
                _isError.value = true
                if (response.isSuccessful) {
                    if (response.body()?.get(0)?.data?.isSuccess == 1) {
                        _isError.value = false
                    }else{
                        _isError.value = true
                        Log.e("ProfileViewModel", "onFailure: data index 0 = null")
                        _snackbarError.value = Event("onFailure: data index 0 = null")
                    }
                }else{
                    _isError.value = true
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UpdateUserResponseItem?>?>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }

}