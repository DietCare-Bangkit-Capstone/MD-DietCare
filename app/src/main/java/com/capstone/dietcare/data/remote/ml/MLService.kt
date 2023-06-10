package com.capstone.dietcare.data.remote.ml

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MLService {

    @POST("recnut")
    fun postMLRaw2(
        @Body json : JsonObject
    ): Call<MLModel1Response>

    @POST("recol")
    fun postMlModel1(
        @Body json : JsonObject
    ): Call<MLModel1Response>

    @GET("getrecipe")
    fun getRandom():Call<MLModel1Response>

    @POST("search")
    fun postSearch(
        @Body json : JsonObject
    ): Call<MLModel1Response>

}