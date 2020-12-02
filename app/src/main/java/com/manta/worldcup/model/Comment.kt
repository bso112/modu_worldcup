package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName
import java.util.*

//entity에 해당
data class Comment (
    @SerializedName("id")
    val mId : Long,
    @SerializedName("writer")
    val mWriter : String,
    @SerializedName("content")
    val mContents : String,
    @SerializedName("date")
    val mDate : String,
    @SerializedName("topic_id")
    val mTopicId : Long)
