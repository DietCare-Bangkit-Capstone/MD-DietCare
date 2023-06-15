package com.capstone.dietcare.data.local.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PreferenceViewModel(private val pref : LoginPreference): ViewModel() {
    fun isLogin() : LiveData<LoginModel> {
        return pref.isLogin().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun prefUpdate(email:String, password: String){
        viewModelScope.launch {
            pref.login(LoginModel(true,email, password))
        }
    }
}