package com.capstone.dietcare.ui.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.helper.Event
import com.capstone.dietcare.data.remote.ml.MLConfig
import com.capstone.dietcare.data.remote.ml.MLModel1Response
import com.capstone.dietcare.data.remote.news.DataItem
import com.capstone.dietcare.data.remote.news.NewsConfig
import com.capstone.dietcare.data.remote.news.NewsResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (context: Context): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    private val _listNews = MutableLiveData<List<DataItem>?>()
    val listNews: MutableLiveData<List<DataItem>?> = _listNews

    private val _listRandom = MutableLiveData<List<com.capstone.dietcare.data.remote.ml.DataItem>?>()
    val listRandom: MutableLiveData<List<com.capstone.dietcare.data.remote.ml.DataItem>?> = _listRandom

    private val _listSearch = MutableLiveData<List<com.capstone.dietcare.data.remote.ml.DataItem>?>()
    val listSearch: MutableLiveData<List<com.capstone.dietcare.data.remote.ml.DataItem>?> = _listSearch

    fun getRandomRecipe(){
        _isLoading.value = true
        val client = MLConfig.getApiService().getRandom()
        client.enqueue(object : Callback<MLModel1Response>{
            override fun onResponse(
                call: Call<MLModel1Response>,
                response: Response<MLModel1Response>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _listRandom.value = response.body()?.data
                    }
                }else{
                    Log.e("MainViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MLModel1Response>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }

    fun postSearch(body: JsonObject){
        _isLoading.value =true
        _isError.value = false
        val client = MLConfig.getApiService().postSearch(body)
        client.enqueue(object : Callback<MLModel1Response>{
            override fun onResponse(
                call: Call<MLModel1Response>,
                response: Response<MLModel1Response>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _listSearch.value = response.body()?.data
                        if (response.body()?.data?.size == 0){
                            _isError.value = true
                        }
                    }
                }else{
                    _isError.value = true
                    Log.e("MainViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MLModel1Response>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e("MainViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })

    }

    fun getAllNews(){
        _isLoading.value =true
        val client = NewsConfig.getApiService().getNews()
        client.enqueue(object : Callback<NewsResponse>{
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    if (response.body() != null){
                        _listNews.value = response.body()?.data
                    }
                }else{
                    Log.e("MainViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }
}