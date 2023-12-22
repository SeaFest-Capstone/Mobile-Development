package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class ViewCartResponse(

	@field:SerializedName("bookmarks")
	val bookmarks: List<BookmarksItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class BookmarksItem(

	@field:SerializedName("cart")
	val cart: Cart? = null,

	@field:SerializedName("fishData")
	val fishData: FishData? = null
)

data class Cart(

	@field:SerializedName("fishIdCart")
	val fishIdCart: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class FishData(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

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
	val benefit: String? = null
)
