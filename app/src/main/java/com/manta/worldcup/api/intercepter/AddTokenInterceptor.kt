package com.manta.worldcup.api.intercepter

import android.content.Context
import com.manta.worldcup.helper.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * http 요청시 액세스토큰(웹서버에 로그인을 인증하기 위한 토큰)을 추가하는 인터셉터
 */
class AddTokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //리퀘스트빌더
        val builder: Request.Builder = chain.request().newBuilder()
        //저장된 토큰을 가져온다.
        val pref = context.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
        val token = pref.getString(Constants.PREF_TOKEN, "");
        if (token != null && token != "")
            builder.addHeader("Token", token);

        return chain.proceed(builder.build())
    }
}
