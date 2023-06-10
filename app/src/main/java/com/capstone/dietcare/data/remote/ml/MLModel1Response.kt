package com.capstone.dietcare.data.remote.ml

import com.google.gson.annotations.SerializedName

data class MLModel1Response(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItem(

	@field:SerializedName("CookTime")
	val cookTime: String? = null,

	@field:SerializedName("RecipeInstructions")
	val recipeInstructions: String? = null,

	@field:SerializedName("Images")
	val images: String? = null,

	@field:SerializedName("RecipeIngredientParts")
	val recipeIngredientParts: String? = null,

	@field:SerializedName("SodiumContent")
	val sodiumContent: Any? = null,

	@field:SerializedName("SugarContent")
	val sugarContent: Any? = null,

	@field:SerializedName("Calories")
	val calories: Any? = null,

	@field:SerializedName("CholesterolContent")
	val cholesterolContent: Any? = null,

	@field:SerializedName("Name")
    var name: String? = null,

	@field:SerializedName("PrepTime")
	val prepTime: String? = null,

	@field:SerializedName("RecipeServings")
	val recipeServings: Any? = null,

	@field:SerializedName("TotalTime")
	val totalTime: String? = null,

	@field:SerializedName("SaturatedFatContent")
	val saturatedFatContent: Any? = null,

	@field:SerializedName("CarbohydrateContent")
	val carbohydrateContent: Any? = null,

	@field:SerializedName("FatContent")
	val fatContent: Any? = null,

	@field:SerializedName("ProteinContent")
	val proteinContent: Any? = null,

	@field:SerializedName("FiberContent")
	val fiberContent: Any? = null
)
