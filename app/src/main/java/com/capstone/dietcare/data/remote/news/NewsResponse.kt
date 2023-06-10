package com.capstone.dietcare.data.remote.news

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Image(

	@field:SerializedName("small")
	val small: String? = null,

	@field:SerializedName("large")
	val large: String? = null
)

data class DataItem(

	@field:SerializedName("image")
	val image: Image? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("isoDate")
	val isoDate: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("contentSnippet")
	val contentSnippet: String? = null
)
