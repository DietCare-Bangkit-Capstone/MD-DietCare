package com.capstone.dietcare.data.remote.news

import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("health")
    fun getNews(): Call<NewsResponse>

}