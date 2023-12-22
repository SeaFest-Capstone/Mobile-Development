package com.example.seafest.adapter

import com.google.gson.annotations.SerializedName

data class AddScanResponse(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
