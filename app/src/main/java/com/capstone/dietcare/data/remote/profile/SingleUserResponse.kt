package com.capstone.dietcare.data.remote.profile

import com.google.gson.annotations.SerializedName

data class SingleUserResponse(

	@field:SerializedName("SingleUserResponse")
	val singleUserResponse: List<SingleUserResponseItem?>? = null
)

data class SingleUserResponseItem(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("sex")
	val sex: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("weight")
	val weight: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("height")
	val height: Any? = null
)
