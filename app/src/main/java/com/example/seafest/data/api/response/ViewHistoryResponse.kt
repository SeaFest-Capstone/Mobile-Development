package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class ViewHistoryResponse(

	@field:SerializedName("ViewHistoryResponse")
	val viewHistoryResponse: List<ViewHistoryResponseItem?>? = null
)

data class ViewHistoryResponseItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("fishName")
	val fishName: String? = null,

	@field:SerializedName("scanId")
	val scanId: String? = null,

	@field:SerializedName("fishStatus")
	val fishStatus: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("scanDate")
	val scanDate: String? = null
)
