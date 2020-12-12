package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName

data class TopicJoinUser (
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
    val mManagerEmail : String,
    @SerializedName("tier")
    val mTier : Int,
    @SerializedName("point")
    val mCurrPoint : Int){

    fun getTopic() : Topic = Topic(mId, mTitle, mDescription, mManagerName, mImageLength, mManagerEmail)
}
