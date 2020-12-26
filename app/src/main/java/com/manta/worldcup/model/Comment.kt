package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName
import java.util.*

//entity에 해당

data class Comment (
    @SerializedName("id")
    val mId : Long,
    @SerializedName("writer")
    val mWriter : String,
    @SerializedName("writer_email")
    val mWriterEmail : String,
    @SerializedName("content")
    val mContents : String,
    @SerializedName("date")
    val mDate : String,
    /**
     * by 변성욱
     * 이 코멘트가 붙는 요소의 id를 말한다.
     * topic의 코멘트면 topic의 id.
     * picture의 코멘트면 picture의 id
     */
    @SerializedName("content_id")
    val mAttachedContentID : Long,
    @SerializedName("content_owner_email")
    val mContentOwnerEamil : String,
    /**
     * 이 댓글의 부모 댓글의 id를 의미한다.
     */
    @SerializedName("parent_id")
    val mParentID : Long? = null,
    @SerializedName("recommend")
    var mRecommend : Int = 0

)

