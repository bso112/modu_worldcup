package com.manta.worldcup.api

import com.manta.worldcup.adapter.RECOMMEND
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
    @POST("comment/picture/update_recommend")
    suspend fun updatePictureCommentRecommend(@Field("comment_id") commentID : Long,
                                              @Field("good") good: Int,
                                              @Field("bad") bad : Int)

    @POST("comment/picture/delete")
    suspend fun deletePictureComment(@Body comment : Comment) : Response<List<Comment>>


    @POST("comment/picture/update")
    suspend fun updatePictureComment(@Body comment : Comment) : Response<List<Comment>>

    @FormUrlEncoded
    @POST("comment/topic/update_recommend")
    suspend fun updateTopicCommentRecommend(@Field("comment_id") commentID : Long,
                                              @Field("good") good: Int,
                                              @Field("bad") bad : Int)

    @POST("comment/topic/delete")
    suspend fun deleteTopicComment(@Body comment : Comment) : Response<List<Comment>>


    @POST("comment/topic/update")
    suspend fun updateTopicComment(@Body comment : Comment) : Response<List<Comment>>


}
