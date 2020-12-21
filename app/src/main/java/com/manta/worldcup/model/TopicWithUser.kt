package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName

data class TopicJoinUser (
    @SerializedName("id")
    val mId: Long,
    @SerializedName("date")
    val mDate : String,
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
    val mCurrPoint : Int,
    @SerializedName("like")
    val mLike : Int = 0,
    @SerializedName("dislike")
    val mDislike : Int = 0,
    @SerializedName("view")
    val mView : Int = 0){

    fun getTopic() : Topic = Topic(mId, mDate, mTitle, mDescription, mManagerName, mImageLength, mManagerEmail, mLike, mDislike, mView);
}
