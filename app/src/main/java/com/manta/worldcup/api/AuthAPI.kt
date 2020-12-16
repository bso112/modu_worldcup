package com.manta.worldcup.api

import com.manta.worldcup.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthAPI {
    @FormUrlEncoded
    @POST("auth/verify_idToken")
    suspend fun sendIdToken(@Field("token") token : String) : Response<String>

    @POST("auth/verify_accessToken")
    suspend fun sendAceessToken() : Response<User>

    @FormUrlEncoded
    @POST("firebase_token/register")
    suspend fun registerFirebaseToken(@Field("email") userEmail : String, @Field("token") token : String);
}