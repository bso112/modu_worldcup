package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * 월드컵과 유저데이터를 조인한 자료이다.
 * -> 리팩토링 : 이렇게 하기보다는 Topic, User 로 가지고 있는게 나을듯
 * 그러려면 TypeConverter를 만들어야함. 예를들어, Topic을 json string으로 json string을 Topic으로 만드는
 * TypeConverter가 있으면 커스텀 클래스를 저장하는 것도 가능함.
 */
data class TopicJoinUser(
    @SerializedName("id")
    val mId: Long,
    @SerializedName("date")
    val mDate: String,
    @SerializedName("title")
    val mTitle: String,
    @SerializedName("description")
    val mDescription: String,
    @SerializedName("manager")
    val mManagerName: String,
    @SerializedName("image_length")
    val mImageLength: Int,
    @SerializedName("manager_email")
    val mManagerEmail: String,
    @SerializedName("tier")
    val mTier: Int,
    @SerializedName("point")
    val mCurrPoint: Int,
    @SerializedName("like")
    val mLike: Int = 0,
    @SerializedName("dislike")
    val mDislike: Int = 0,
    @SerializedName("view")
    val mView: Int = 0,
    @SerializedName("profile_path")
    val mProfileImgName: String = ""
) : Serializable {

    fun getTopic(): Topic = Topic(mId, mDate, mTitle, mDescription, mManagerName, mImageLength, mManagerEmail, mLike, mDislike, mView);
    fun getUser(): User = User(mManagerEmail, mManagerName, mTier, mCurrPoint, mProfileImgName)
}
