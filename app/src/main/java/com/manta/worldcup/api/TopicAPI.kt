package com.manta.worldcup.api

import android.graphics.Bitmap
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.Picture
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.TopicModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

//Dao에 해당
interface TopicAPI {

    @Multipart
    @POST("topic/new")
    suspend fun insertTopic(@Part("topic") topicModel : TopicModel, @Part("pictures") pictures : List<PictureModel>, @Part image :  List<MultipartBody.Part>)  : Response<String>

    @GET("topic/get_all")
    suspend fun getAllTopic() : Response<ArrayList<TopicModel>>

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

    @GET("pictures/get/{topic_id}")
    suspend fun getPictures(@Path("topic_id") topicId : Long) : Response<List<PictureModel>>

    @GET("comment/get_all/{topic_id}")
    suspend fun getComments(@Path("topic_id") topicId : Long) : Response<ArrayList<Comment>>

    /**
     * by 변성욱
     * 코멘트를 생성한뒤, 응답으로 새로운 코멘트 리스트를 받아온다.
     */
    @POST("comment/new")
    suspend fun insertComment(@Body comment : Comment) : Response<String>
}