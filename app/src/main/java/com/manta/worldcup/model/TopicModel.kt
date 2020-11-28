package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName

data class TopicModel(
    @SerializedName("id")
    val mId: Long,
    @SerializedName("title")
    val mTitle: String,
    @SerializedName("description")
    val mDescription : String,
    @SerializedName("manager")
    val mManagerName: String,
    @SerializedName("image_length")
    val mImageLength : Int
)

data class Topic
    (
    val mTopicModel : TopicModel,
    val mComments: List<Comment>,
    val mPictureModels: List<PictureModel>
) {

    /**
     * 토픽에 해당 이름과 동일한 사진이 없는가?
     * @param pictureName 검사할 이름
     */
    fun isPictureUnique(pictureName: String): Boolean {
        return true;
    }
}