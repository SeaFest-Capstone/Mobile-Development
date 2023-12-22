package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class AddScanResponse(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
