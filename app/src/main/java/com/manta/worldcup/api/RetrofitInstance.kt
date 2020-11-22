package com.manta.worldcup.api

import com.manta.worldcup.helper.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Database에 해당. 인터페이스인 api를 retrofit으로 구현하고, 하나만 생성되게 보장한다.
object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    val api : SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java);
    }


}