package com.example.seafest.data.api.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("listScan")
	val listScan: List<ListScanItem?>? = null
)

data class ListScanItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("fishName")
	val fishName: String? = null,

	@field:SerializedName("habitat")
	val habitat: String? = null,

	@field:SerializedName("scanId")
	val scanId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("fishStatus")
	val fishStatus: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("benefit")
	val benefit: String? = null,

	@field:SerializedName("scanDate")
	val scanDate: String? = null
)
