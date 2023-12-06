package com.example.seafest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fish")
data class FishEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: Int?,

    @SerializedName("photoUrl")
    val photoUrl: String? = null,

    @SerializedName("habitat")
    val habitat: String? = null,

    @SerializedName("price")
    val price: Int? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("idHabitate")
    val idHabitate: Int? = null,

    @SerializedName("nameFish")
    val nameFish: String? = null,

    @SerializedName("benefit")
    val benefit: String? = null
)