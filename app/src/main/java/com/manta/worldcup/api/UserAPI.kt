package com.manta.worldcup.api

import com.manta.worldcup.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    @FormUrlEncoded
    @POST("user/update_nickname")
    suspend fun updateUserNickname(@Field("nickname") nickname : String, @Field("email") email : String) : Response<Int>

    /**
     * @param fileName 기존 유저가 가지고있던 프로필 이미지 파일 이름
     */
    @Multipart
    @POST("profile_image/upload")
    suspend fun uploadProfileImage(@Part("user_email") userEmail : String,@Part("file_name") fileName : String, @Part image : MultipartBody.Part) : Response<Int>

    @POST("user/upate")
    suspend fun updateUser(@Body user : User)

    @FormUrlEncoded
    @POST("user/remove_profile_image")
    suspend fun removeProfileImage(@Field("user_email") userEmail: String, @Field("old_file_name") oldImageName : String) : Response<Int>
}