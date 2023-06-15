package com.capstone.dietcare.data.remote.profile

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("UpdateUserResponse")
	val updateUserResponse: List<UpdateUserResponseItem?>? = null
)

data class Data(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Int? = null
)

data class UpdateUserResponseItem(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)
