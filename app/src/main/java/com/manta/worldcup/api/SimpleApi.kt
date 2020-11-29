package com.manta.worldcup.api

import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.TopicModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

//Dao에 해당
interface SimpleApi {

    @Multipart
    @POST("topic/new")
    suspend fun insertTopic(@Part("topic") topicModel : TopicModel, @Part("pictures") pictures : List<PictureModel>, @Part image :  List<MultipartBody.Part>)  : Response<String>

    @GET("topic/get_all")
    suspend fun getAllTopic() : Response<List<TopicModel>>

    @GET("topic/get/{topic_id}")
    suspend fun getTopic(@Path("topic_id")topicId : Long) : Response<TopicModel>

    @Multipart
    @POST("picture/new")
    suspend fun insertPictures(@Part("topic_id") topic_id : Long, @Part("pictures") pictures : List<PictureModel>, @Part image :  List<MultipartBody.Part>) : Response<String>;

    /**
     * by 변성욱
     * 토픽id와 관련된 picture들의 이름을 얻는다.
     */
    @GET("picture/get_names/{topic_id}")
    suspend fun getPicturesName(@Path("topic_id") topicId : Long) : Response<HashSet<String>>

    @GET("picture/get/{topic_id}/{picture_index}")
    suspend fun getTopicPicture(@Path("topic_id") topicId : Long, @Path("picture_index") pictureIndex :Int) : Response<PictureModel>


}