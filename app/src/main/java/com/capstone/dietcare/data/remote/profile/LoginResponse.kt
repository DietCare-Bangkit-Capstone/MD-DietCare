package com.capstone.dietcare.data.remote.profile

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("LoginResponse")
	val loginResponse: List<LoginResponseItem?>? = null
)

data class DataLogin(

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class LoginResponseItem(

	@field:SerializedName("data")
	val data: DataLogin? = null,

	@field:SerializedName("message")
	val message: String? = null
)
