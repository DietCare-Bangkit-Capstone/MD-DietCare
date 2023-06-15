package com.capstone.dietcare.data.remote.profile

data class DeleteProfileResponse(
	val deleteProfileResponse: List<DeleteProfileResponseItem?>? = null
)

data class DeleteProfileResponseItem(
	val data: DataDelete? = null,
	val message: String? = null
)

data class DataDelete(
	val isDeleted: Int? = null
)

