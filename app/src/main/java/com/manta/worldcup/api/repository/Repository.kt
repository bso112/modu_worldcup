package com.manta.worldcup.api.repository

import android.app.Application
import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import okhttp3.MultipartBody

class Repository(application : Application) {

    private val topicAPI  = RetrofitInstance(application).topicAPI;
    private val authAPI = RetrofitInstance(application).authAPI;
    private val userAPI = RetrofitInstance(application).userAPI;

    suspend fun getAllTopicJoinUser() =  topicAPI.getAllTopicJoinUser();

    suspend fun getTopicJoinUsers(userEmail : String) = topicAPI.getTopicJoinUsers(userEmail)

    suspend fun  insertTopic(topic: Topic, pictures : List<PictureModel>, image :  List<MultipartBody.Part>) = topicAPI.insertTopic(topic,pictures, image);

    suspend fun  getAllPictures(ownerEmail : String) = topicAPI.getPictures(ownerEmail);

    suspend fun  insertPictures(topic_id :Long , pictures : List<PictureModel> , image :  List<MultipartBody.Part>) = topicAPI.insertPictures(topic_id ,pictures, image);

    suspend fun getPicturesName(topic_id :Long) = topicAPI.getPicturesName(topic_id);

    suspend fun getPictures(topic_id: Long) = topicAPI.getPictures(topic_id);

    suspend fun getTopicComment(topic_id: Long) = topicAPI.getTopicComments(topic_id);

    suspend fun getPictureComment(picture_id : Long) = topicAPI.getPictureComments(picture_id);

    suspend fun insertTopicComment(comment : Comment) = topicAPI.insertTopicComment(comment);

    suspend fun insertPictureComment(comment : Comment) = topicAPI.insertPictureComment(comment);


    suspend fun sendIdToken(token : String) = authAPI.sendIdToken(token);

    suspend fun sendAccessToken() = authAPI.sendAceessToken();

    suspend fun registerFirebaseToken(userEmail : String,  token : String) = authAPI.registerFirebaseToken(userEmail, token)

    suspend fun addPoint(amount : Int, email : String) = topicAPI.addPoint(amount, email);

    suspend fun addwinCnt(pictureID : Long) = topicAPI.addWinCnt(pictureID);

    suspend fun getUser(email : String) = topicAPI.getUser(email);

    suspend fun updateRecommned(like : Boolean, topicId : Long) = topicAPI.updateRecommend(like, topicId);

    suspend fun increaseView( topicId : Long) = topicAPI.increaseView(topicId);

    suspend fun updateUserNickname(nickname : String, email : String) = userAPI.updateUserNickname(nickname, email)
}