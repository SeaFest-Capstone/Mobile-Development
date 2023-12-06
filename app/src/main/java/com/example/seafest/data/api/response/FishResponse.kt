package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class FishResponse(

	@field:SerializedName("listFish")
	val listFish: List<ListFishItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListFishItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("habitat")
	val habitat: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("idHabitate")
	val idHabitate: Int? = null,

	@field:SerializedName("nameFish")
	val nameFish: String? = null,

	@field:SerializedName("benefit")
	val benefit: String? = null
)
