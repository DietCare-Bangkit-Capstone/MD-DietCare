package com.capstone.dietcare.ui.recommendation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dietcare.data.helper.Event
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.data.remote.ml.MLConfig
import com.capstone.dietcare.data.remote.ml.MLModel1Response
import com.capstone.dietcare.data.local.user.DataUser
import com.capstone.dietcare.data.local.user.DataUserRepository
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecViewModel (context: Context) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    private val _listRecommendation = MutableLiveData<List<DataItem>?>()
    val listRecommendation: MutableLiveData<List<DataItem>?> = _listRecommendation


    private val mDataUserRepository: DataUserRepository = DataUserRepository(context)

    fun getLatestMeal(): LiveData<DataUser> = mDataUserRepository.getLatestMeal()

    fun updateMeal1(data: RecIntent){
        mDataUserRepository.updateMeal1(data.name!!, data.img!!, data.cal!!, data.time!!, data.port!!)
        Log.d("rec1", "${data.name}")
    }

    fun updateMeal2(data: RecIntent){
        mDataUserRepository.updateMeal2(data.name, data.img, data.cal, data.time, data.port)
        Log.d("rec2", "${data.name}")
    }

    fun updateMeal3(data: RecIntent){
        mDataUserRepository.updateMeal3(data.name, data.img, data.cal, data.time, data.port)
        Log.d("rec3", "${data.name}")
    }
    fun updateMeal4(data: RecIntent){
        mDataUserRepository.updateMeal4(data.name, data.img, data.cal, data.time, data.port)
    }
    fun updateMeal5(data: RecIntent){
        mDataUserRepository.updateMeal5(data.name, data.img, data.cal, data.time, data.port)
    }

    fun getModel2(body: JsonObject){
        _isLoading.value = true
        _isError.value = false
        val client = MLConfig.getApiService().postMLRaw2(body)
        client.enqueue(object  : Callback<MLModel1Response>{
            override fun onResponse(call: Call<MLModel1Response>, response: Response<MLModel1Response>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listRecommendation.value = response.body()?.data
                    }
                }else{
                    _isError.value = true
                    Log.e("RecViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MLModel1Response>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e("RecViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }

    fun getModel1(body: JsonObject){
        _isLoading.value = true
        _isError.value = false
        val client = MLConfig.getApiService().postMlModel1(body)
        client.enqueue(object  : Callback<MLModel1Response>{
            override fun onResponse(call: Call<MLModel1Response>, response: Response<MLModel1Response>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listRecommendation.value = response.body()?.data
                    }
                }else{
                    _isError.value = true
                    Log.e("RecViewModel", "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MLModel1Response>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e("RecViewModel", "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }
        })
    }
}