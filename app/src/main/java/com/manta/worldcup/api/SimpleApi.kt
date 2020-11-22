package com.manta.worldcup.api

import com.manta.worldcup.model.Topic
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//Dao에 해당
interface SimpleApi {

    @POST("topic/new")
    suspend fun insertTopic(@Body topic : Topic)

    @GET("topic/get_all")
    suspend fun getAllTopic() : Response<List<Topic>>


}