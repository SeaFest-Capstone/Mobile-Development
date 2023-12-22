package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class CartResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("cartItemId")
	val cartItemId: String? = null
)
