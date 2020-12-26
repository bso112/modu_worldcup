package com.manta.worldcup.api

import com.manta.worldcup.model.Comment
import retrofit2.Response
import retrofit2.http.*

interface CommentAPI {

    @GET("comment/topic/get_all/{topic_id}")
    suspend fun getTopicComments(@Path("topic_id") topicId : Long) : Response<ArrayList<Comment>>

    @GET("comment/picture/get_all/{picture_id}")
    suspend fun getPictureComments(@Path("picture_id") topicId : Long) : Response<ArrayList<Comment>>

    /**
     * by 변성욱
     * 코멘트를 생성한뒤, 응답으로 새로운 코멘트 리스트를 받아온다.
     */
    @POST("comment/topic/new")
    suspend fun insertTopicComment(@Body comment : Comment) : Response<ArrayList<Comment>>

    /**
     * @return 새로운 코멘트리스트
     */
    @POST("comment/picture/new")
    suspend fun insertPictureComment(@Body comment : Comment) : Response<ArrayList<Comment>>

    @FormUrlEncoded
    @POST("picture/comment/recommend")
    suspend fun addRecommend(@Field("comment_id") commentID : Long)
}