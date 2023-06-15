package com.capstone.dietcare.data.remote.profile

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("RegisterResponse")
	val registerResponse: List<RegisterResponseItem?>? = null
)

data class DataRegister(

	@field:SerializedName("isSuccess")
	val isSuccess: Int? = null
)

data class RegisterResponseItem(

	@field:SerializedName("data")
	val data: DataRegister? = null,

	@field:SerializedName("message")
	val message: String? = null
)
