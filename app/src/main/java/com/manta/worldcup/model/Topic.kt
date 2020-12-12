package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Topic(
    @SerializedName("id")
    val mId: Long,
    @SerializedName("title")
    val mTitle: String,
    @SerializedName("description")
    val mDescription : String,
    @SerializedName("manager")
    val mManagerName: String,
    @SerializedName("image_length")
    val mImageLength : Int,
    @SerializedName("manager_email")
    val mManagerEmail : String
) : Serializable
