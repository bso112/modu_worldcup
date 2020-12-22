package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Topic(
    @SerializedName("id")
    val mId: Long,
    @SerializedName("date")
    val mDate : String,
    @SerializedName("title")
    var mTitle: String,
    @SerializedName("description")
    var mDescription : String,
    @SerializedName("manager")
    val mManagerName: String,
    @SerializedName("image_length")
    var mImageLength : Int,
    @SerializedName("manager_email")
    val mManagerEmail : String,
    @SerializedName("like")
    val mLike : Int = 0,
    @SerializedName("dislike")
    val mDislike : Int = 0,
    @SerializedName("view")
    val mView : Int = 0
) : Serializable
