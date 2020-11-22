package com.manta.worldcup.repository

import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.Topic
import retrofit2.Response

class Repository() {

    suspend fun getAllTopic() : Response<List<Topic>> {
        return RetrofitInstance.api.getAllTopic();
    }






}