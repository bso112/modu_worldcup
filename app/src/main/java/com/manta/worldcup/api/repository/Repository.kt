package com.manta.worldcup.api.repository

import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.TopicModel
import okhttp3.MultipartBody
import retrofit2.Response

class Repository() {

    private val api  = RetrofitInstance.api;

    suspend fun getTopic(topicId : Int) : Response<TopicModel> = api.getTopic(topicId);

    suspend fun getAllTopic() : Response<List<TopicModel>> = api.getAllTopic();

   // suspend fun  insertTopic(image :  List<MultipartBody.Part>) = api.insertTopic(image);

    suspend fun  insertTopic(topicModel: TopicModel, pictures : List<PictureModel> ,image :  List<MultipartBody.Part>) = api.insertTopic(topicModel,pictures, image);

    suspend fun  insertPictures(topic_id :Long , pictures : List<PictureModel> , image :  List<MultipartBody.Part>) = api.insertPictures(topic_id ,pictures, image);

    suspend fun getPicturesName(topic_id :Long) = api.getPicturesName(topic_id);






}