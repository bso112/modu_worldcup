package com.manta.worldcup.api.repository

import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.Topic
import okhttp3.MultipartBody
import retrofit2.Response

class Repository() {

    private val api  = RetrofitInstance.api;

    suspend fun getTopic(topicId : Int) : Response<Topic> = api.getTopic(topicId);

    suspend fun getAllTopic() : Response<List<Topic>> = api.getAllTopic();

   // suspend fun  insertTopic(image :  List<MultipartBody.Part>) = api.insertTopic(image);

    suspend fun  insertTopic(image :  List<MultipartBody.Part>) = api.insertTopic(image);

    suspend fun  echo(string : String) = api.echo()







}