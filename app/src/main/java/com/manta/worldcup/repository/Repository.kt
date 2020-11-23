package com.manta.worldcup.repository

import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.Topic
import retrofit2.Response

class Repository() {

    private val api  = RetrofitInstance.api;

    suspend fun getTopic(topicId : Int) : Response<Topic> = api.getTopic(topicId);

    suspend fun getAllTopic() : Response<List<Topic>> = api.getAllTopic();

    suspend fun  insertTopic(topic : Topic) = api.insertTopic(topic);







}