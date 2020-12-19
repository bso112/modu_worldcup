package com.manta.worldcup.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {

    @FormUrlEncoded
    @POST("user/update_nickname")
    suspend fun updateUserNickname(@Field("nickname") nickname : String, @Field("email") email : String) : Response<Int>
}