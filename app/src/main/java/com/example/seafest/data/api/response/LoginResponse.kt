package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("uid")
	val uid: String? = null,


	@field:SerializedName("token")
	val token: String? = null
)
