package com.manta.worldcup.api.repository

import android.content.Context
import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.model.*
import okhttp3.MultipartBody

class Repository(application: Context) {

    private val topicAPI = RetrofitInstance(application).topicAPI;
    private val authAPI = RetrofitInstance(application).authAPI;
    private val userAPI = RetrofitInstance(application).userAPI;
    private val commentAPI = RetrofitInstance(application).commentAPI;

    suspend fun getAllTopicJoinUser() = topicAPI.getAllTopicJoinUser();

    suspend fun getTopicJoinUsers(userEmail: String) = topicAPI.getTopicJoinUsers(userEmail)

    suspend fun getTopicJoinUser(topicID : Long) = topicAPI.getTopicJoinUser(topicID)

    suspend fun insertTopic(topic: Topic, pictures: List<PictureModel>, image: List<MultipartBody.Part>) = topicAPI.insertTopic(topic, pictures, image);

    suspend fun deleteTopic(topicID: Long) = topicAPI.deleteTopic(topicID);

    suspend fun updateTopic(topic: Topic) = topicAPI.updateTopic(topic);

    suspend fun getTopicName(topicID: Long) = topicAPI.getTopicName(topicID);

    /**
     * topic의 imagelength를 줄임
     */
    suspend fun deleteMyPictures(pictureIDs: List<PictureModel>) = topicAPI.deleteMyPictures(pictureIDs);

    /**
     * topic의 imagelength를 줄이지 않음
     */
    suspend fun deletePictures(pictureIDs: List<PictureModel>) = topicAPI.deletePictures(pictureIDs);


    suspend fun getAllPictures(ownerEmail: String) = topicAPI.getPictures(ownerEmail);

    suspend fun insertPictures(topic_id: Long, pictures: List<PictureModel>, image: List<MultipartBody.Part>) = topicAPI.insertPictures(topic_id, pictures, image);

    suspend fun getPicturesName(topic_id: Long) = topicAPI.getPicturesName(topic_id);

    suspend fun getPictures(topic_id: Long) = topicAPI.getPictures(topic_id);

    suspend fun getTopicComment(topic_id: Long) = commentAPI.getTopicComments(topic_id);

    suspend fun getPictureComment(picture_id: Long) = commentAPI.getPictureComments(picture_id);

    suspend fun insertTopicComment(comment: Comment) = commentAPI.insertTopicComment(comment);

    suspend fun insertPictureComment(comment: Comment) = commentAPI.insertPictureComment(comment);


    suspend fun sendIdToken(token: String) = authAPI.sendIdToken(token);

    suspend fun sendAccessToken() = authAPI.sendAceessToken();

    suspend fun registerFirebaseToken(userEmail: String, token: String) = authAPI.registerFirebaseToken(userEmail, token)

    suspend fun addPoint(amount: Int, email: String) = topicAPI.addPoint(amount, email);

    suspend fun addwinCnt(pictureID: Long) = topicAPI.addWinCnt(pictureID);

    suspend fun getUser(email: String) = topicAPI.getUser(email);

    suspend fun updateTopicRecommend(like: Boolean, topicId: Long) = topicAPI.updateRecommend(like, topicId);


    suspend fun increaseView(topicId: Long) = topicAPI.increaseView(topicId);

    suspend fun updateUserNickname(nickname: String, email: String) = userAPI.updateUserNickname(nickname, email)

    suspend fun updatePictureName(pictureID: Long, name: String) = topicAPI.updatePictureName(pictureID, name)

    suspend fun deletePictureComment(comment: Comment) = commentAPI.deletePictureComment(comment)

    suspend fun updatePictureComment(comment: Comment) = commentAPI.updatePictureComment(comment)

    suspend fun updatePictureCommentRecommend(commentID: Long, good: Int, bad: Int) = commentAPI.updatePictureCommentRecommend(commentID, good, bad)

    suspend fun deleteTopicComment(comment: Comment) = commentAPI.deleteTopicComment(comment)

    suspend fun updateTopicComment(comment: Comment) = commentAPI.updateTopicComment(comment)

    suspend fun updateTopicCommentRecommend(commentID: Long, good: Int, bad: Int) = commentAPI.updateTopicCommentRecommend(commentID, good, bad)

    suspend fun reportTopic(topicJoinUser: TopicJoinUser) = topicAPI.reportTopic(topicJoinUser)

    suspend fun uploadProfileImage(userEmail : String, fileName : String, image : MultipartBody.Part) = userAPI.uploadProfileImage(userEmail, fileName, image)

    suspend fun updateUser(user : User) = userAPI.updateUser(user)

    suspend fun removeProfileImage(userEmail : String, oldImageName : String) = userAPI.removeProfileImage(userEmail, oldImageName)
}