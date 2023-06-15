package com.capstone.dietcare.data.remote.profile

import com.capstone.dietcare.data.remote.ml.MLModel1Response
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {

    @GET("users/{emailUser}")
    fun getSingleUser(
        @Path("emailUser") emailUser: String
    ): Call<List<SingleUserResponseItem?>?>

    @PUT("users/{emailUser}")
    fun updateUser(
        @Path("emailUser") emailUser: String,
        @Body json : JsonObject
    ): Call<List<UpdateUserResponseItem?>?>

    @POST("register")
    fun registerUser(
        @Body json : JsonObject
    ): Call<List<RegisterResponseItem?>?>

    @POST("login")
    fun loginUser(
        @Body json : JsonObject
    ): Call<List<LoginResponseItem?>?>

    @HTTP(method = "DELETE", path = "users", hasBody = true)
    fun deleteUser(
        @Body json : JsonObject
    ): Call<List<DeleteProfileResponseItem?>?>

}