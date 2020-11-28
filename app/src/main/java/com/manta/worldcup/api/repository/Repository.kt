package com.manta.worldcup.api.repository

import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.TopicModel
import okhttp3.MultipartBody
import retrofit2.Response

class Repository() {

    private val api  = RetrofitInstance.api;

    suspend fun getTopic(topicId : Int) : Response<TopicModel> = api.getTopic(topicId);

    suspend fun getAllTopic() : Response<List<TopicModel>> = api.getAllTopic();

   // suspend fun  insertTopic(image :  List<MultipartBody.Part>) = api.insertTopic(image);

    suspend fun  insertTopic(topicModel: TopicModel, image :  List<MultipartBody.Part>) = api.insertTopic(topicModel, image);








}