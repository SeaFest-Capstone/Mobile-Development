package com.example.seafest.adapter

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null
)
