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

    @Multipart
    @POST("profile_image/upload")
    suspend fun uploadProfileImage(@Part("user_email") userEmail : String, @Part image : MultipartBody.Part) : Response<Int>

    @POST("user/upate")
    suspend fun updateUser(@Body user : User)
}