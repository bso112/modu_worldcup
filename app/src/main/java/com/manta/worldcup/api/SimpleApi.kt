package com.manta.worldcup.api

import com.manta.worldcup.model.TopicModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

//Dao에 해당
interface SimpleApi {

//    @Multipart
//    @POST("anime/topic/new")
//    suspend fun insertTopic(@Part image :  List<MultipartBody.Part>)

    @Multipart
    @POST("topic/new")
    suspend fun insertTopic(@Part("topic") topicModel : TopicModel, @Part image :  List<MultipartBody.Part>)  : Response<String>

    @GET("topic/get_all")
    suspend fun getAllTopic() : Response<List<TopicModel>>

    @GET("topic/get")
    suspend fun getTopic(topicId : Int) : Response<TopicModel>



}