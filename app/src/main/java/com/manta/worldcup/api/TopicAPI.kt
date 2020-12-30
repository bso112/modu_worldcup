package com.manta.worldcup.api

import com.manta.worldcup.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

//Dao에 해당
interface TopicAPI {

    @Multipart
    @POST("topic/new")
    suspend fun insertTopic(@Part("topic") topic : Topic, @Part("pictures") pictures : List<PictureModel>, @Part image :  List<MultipartBody.Part>)  : Response<String>

    @FormUrlEncoded
    @POST("topic/delete")
    suspend fun deleteTopic(@Field("topic_id") topicId : Long) : Response<Int>

    @POST("topic/update")
    suspend fun updateTopic(@Body topic : Topic) : Response<Int>

    @GET("topic/get_name/{topic_id}")
    suspend fun getTopicName(@Path("topic_id") topicID : Long) : Response<String>

    @GET("topic/get/{topic_id}")
    suspend fun getTopicJoinUser(@Path("topic_id") topicID : Long) : Response<TopicJoinUser>


    @POST("topic/report")
    suspend fun  reportTopic(@Body topicJoinUser: TopicJoinUser);

    @GET("topicJoinUser/get_all")
    suspend fun getAllTopicJoinUser() : Response<ArrayList<TopicJoinUser>>

    @GET("topicJoinUser/get/{manager_email}")
    suspend fun getTopicJoinUsers(@Path("manager_email")email : String) : Response<List<TopicJoinUser>>

    @GET("user/get/{email}")
    suspend fun getUser(@Path("email") email : String) : Response<User>

    @Multipart
    @POST("picture/new")
    suspend fun insertPictures(@Part("topic_id") topic_id : Long, @Part("pictures") pictures : List<PictureModel>, @Part image :  List<MultipartBody.Part>) : Response<String>;


    /**
     * by 변성욱
     * 토픽id와 관련된 picture들의 이름을 얻는다.
     */
    @GET("picture/get_names/{topic_id}")
    suspend fun getPicturesName(@Path("topic_id") topicId : Long) : Response<HashSet<String>>

    @GET("pictures/get/{topic_id}")
    suspend fun getPictures(@Path("topic_id") topicId : Long) : Response<List<PictureModel>>

    @GET("pictures/get_all/{owner_email}")
    suspend fun getPictures(@Path("owner_email") ownerEmail : String) : Response<List<PictureModel>>

    @POST("pictures/delete")
    suspend fun deletePictures(@Body pictures : List<PictureModel>) : Response<Int>

    @FormUrlEncoded
    @POST("picture/rename")
    suspend fun updatePictureName(@Field("picture_id") pictureID : Long, @Field("name") name : String)




    @FormUrlEncoded
    @POST("picture/add_winCnt")
    suspend fun addWinCnt(@Field("picture_id") pictureID : Long);


    @FormUrlEncoded
    @POST("point/consume")
    suspend fun addPoint(@Field("amount") amount : Int, @Field("email") email : String);

    @FormUrlEncoded
    @POST("topic/recommend")
    suspend fun updateRecommend(@Field("like") like : Boolean, @Field("topic_id") topicId : Long);

    @FormUrlEncoded
    @POST("topic/increase_view")
    suspend fun increaseView(@Field("topic_id") topicId : Long);


}