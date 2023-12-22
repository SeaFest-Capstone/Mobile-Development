package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class FishResponse(

	@field:SerializedName("listFish")
	val listFish: List<ListFishItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListFishItem(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("habitat")
	val habitat: String? = null,

	@field:SerializedName("fishIdCart")
	val fishIdCart: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("nameFish")
	val nameFish: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("benefit")
	val benefit: String? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null
)
