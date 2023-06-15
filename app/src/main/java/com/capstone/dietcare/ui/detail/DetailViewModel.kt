package com.capstone.dietcare.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.local.favorite.FavoriteData
import com.capstone.dietcare.data.local.favorite.FavoriteDataRepository
import com.capstone.dietcare.data.helper.Event
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.data.remote.ml.MLConfig
import com.capstone.dietcare.data.remote.ml.MLModel1Response
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(context: Context): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    private val _itemSearch = MutableLiveData<DataItem>()
    val itemSearch: MutableLiveData<DataItem> = _itemSearch

    private val mFavoriteDataRepository: FavoriteDataRepository = FavoriteDataRepository(context)

    fun isFavorite(name: String, cal: Double): LiveData<FavoriteData> = mFavoriteDataRepository.getIsFavorite(name, cal)

    fun insertFav(data: FavoriteData){
        mFavoriteDataRepository.insert(data)
    }

    fun deleteFav(data: FavoriteData){
        mFavoriteDataRepository.delete(data)
    }

    fun postDetail(body: JsonObject, name: String, cal: Double){
        _isLoading.value =true
        val client = MLConfig.getApiService().postSearch(body)
        client.enqueue(object : Callback<MLModel1Response> {
            override fun onResponse(
                call: Call<MLModel1Response>,
                response: Response<MLModel1Response>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.data?.size != 0 && response.body()?.data?.get(0) != null) {
                        for (i in 0..response.body()?.data?.size!!.minus(1)){
                            if (response.body()?.data?.get(i)!!.name == name && response.body()?.data?.get(i)!!.calories == cal){
                                _itemSearch.value = response.body()?.data?.get(i)
                            }
                        }
                    } else {
                        Log.e("MainViewModel", "Error getting detail: index search data = 0 or data[0] is null")
                        _snackbarError.value = Event("Error getting detail: index search data = 0 or data[0] is null")
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
}