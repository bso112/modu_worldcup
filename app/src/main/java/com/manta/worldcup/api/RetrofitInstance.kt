package com.manta.worldcup.api

import android.content.Context
import com.manta.worldcup.api.intercepter.AddCookiesInterceptor
import com.manta.worldcup.api.intercepter.AddTokenInterceptor
import com.manta.worldcup.api.intercepter.ReceivedCookiesInterceptor
import com.manta.worldcup.helper.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Database에 해당. 인터페이스인 api를 retrofit으로 구현하고, 하나만 생성되게 보장한다.
class RetrofitInstance private constructor(context: Context) {

    companion object{
        private var instance: RetrofitInstance? = null
        fun getInstance(context: Context) : RetrofitInstance  {
            return instance ?: synchronized(this) {
                instance = RetrofitInstance(context);
                return instance as RetrofitInstance; }
        }

    }

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AddCookiesInterceptor(context))
            .addInterceptor(AddTokenInterceptor(context))
            .addInterceptor(ReceivedCookiesInterceptor(context))
            .build();

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    }

    val topicAPI : TopicAPI by lazy {
        retrofit.create(TopicAPI::class.java);
    }

    val authAPI : AuthAPI by lazy{
        retrofit.create(AuthAPI::class.java);
    }

    val userAPI : UserAPI by lazy{
        retrofit.create(UserAPI::class.java)
    }

    val commentAPI : CommentAPI by lazy{
        retrofit.create(CommentAPI::class.java)
    }

}