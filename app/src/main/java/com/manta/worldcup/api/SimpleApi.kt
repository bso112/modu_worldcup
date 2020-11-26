package com.manta.worldcup.api

import com.manta.worldcup.model.Topic
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

//Dao에 해당
interface SimpleApi {

//    @Multipart
//    @POST("anime/topic/new")
//    suspend fun insertTopic(@Part image :  List<MultipartBody.Part>)

    @Multipart
    @POST("anime/topic/new")
    suspend fun insertTopic(@Part image :  List<MultipartBody.Part>)

    @GET("anime/topic/get_all")
    suspend fun getAllTopic() : Response<List<Topic>>

    @GET("anime/topic/get")
    suspend fun getTopic(topicId : Int) : Response<Topic>

    @GET("/")
    suspend fun echo() :Response<String>

    @GET("/{msg}")
    suspend fun echo(@Path("msg") string : String) :Response<String>


}