package com.manta.worldcup.api.repository

import android.app.Application
import android.content.Context
import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.TopicModel
import okhttp3.MultipartBody

class Repository(application : Application) {

    private val topicAPI  = RetrofitInstance(application).topicAPI;
    private val authAPI = RetrofitInstance(application).authAPI;

    suspend fun getAllTopic()  = topicAPI.getAllTopic();

    suspend fun  insertTopic(topicModel: TopicModel, pictures : List<PictureModel> ,image :  List<MultipartBody.Part>) = topicAPI.insertTopic(topicModel,pictures, image);

    suspend fun  insertPictures(topic_id :Long , pictures : List<PictureModel> , image :  List<MultipartBody.Part>) = topicAPI.insertPictures(topic_id ,pictures, image);

    suspend fun getPicturesName(topic_id :Long) = topicAPI.getPicturesName(topic_id);

    suspend fun getPictures(topic_id: Long) = topicAPI.getPictures(topic_id);

    suspend fun getComments(topic_id: Long) = topicAPI.getComments(topic_id);

    suspend fun insertComment(comment : Comment) = topicAPI.insertComment(comment);

    suspend fun sendIdToken(token : String) = authAPI.sendIdToken(token);
    suspend fun sendAccessToken() = authAPI.sendAceessToken();



}