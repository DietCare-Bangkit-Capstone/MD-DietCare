package com.capstone.dietcare.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.capstone.dietcare.data.helper.Event
import com.capstone.dietcare.data.local.login.LoginModel
import com.capstone.dietcare.data.local.login.LoginPreference
import com.capstone.dietcare.data.remote.profile.LoginResponseItem
import com.capstone.dietcare.data.remote.profile.ProfileConfig
import com.capstone.dietcare.data.remote.profile.RegisterResponseItem
import com.capstone.dietcare.data.remote.profile.UpdateUserResponseItem
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val pref : LoginPreference) : ViewModel() {

    companion object{
        private const val TAG = "LoginViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    fun putRegister(body: JsonObject){
        _isLoading.value = true
        val client = ProfileConfig.getApiService().registerUser(body)
        client.enqueue(object : Callback<List<RegisterResponseItem?>?> {
            override fun onResponse(
                call: Call<List<RegisterResponseItem?>?>,
                response: Response<List<RegisterResponseItem?>?>
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

            override fun onFailure(call: Call<List<RegisterResponseItem?>?>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }

    fun login(email:String, password: String){
        _isLoading.value = true
        val user = JsonObject()
        user.addProperty("email", email)
        user.addProperty("password", password)
        val client = ProfileConfig.getApiService().loginUser(user)
        client.enqueue(object : Callback<List<LoginResponseItem?>?> {
            override fun onResponse(
                call: Call<List<LoginResponseItem?>?>,
                response: Response<List<LoginResponseItem?>?>
            ) {
                _isLoading.value = false
                if (response.body()?.get(0)?.data?.success == true){
                    viewModelScope.launch {
                        pref.login(LoginModel(true,email, password))
                    }
                    _isError.value = false
                    _snackbarError.value = Event(response.body()?.get(0)?.message.toString())
                } else {
                    Log.e(TAG, "${response.body()?.get(0)?.message.toString()}")
                    _snackbarError.value = Event("${response.body()?.get(0)?.message.toString()}")
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<LoginResponseItem?>?>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onDetailFailure: ${t.message}")
                _snackbarError.value = Event("onDetailFailure: ${t.message}")
                _isError.value = true
            }

        })
    }

    fun isLogin() : LiveData<LoginModel> {
        return pref.isLogin().asLiveData()
    }
}