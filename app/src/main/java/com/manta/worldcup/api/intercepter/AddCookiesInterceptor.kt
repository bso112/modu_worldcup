package com.manta.worldcup.api.intercepter

import android.content.Context
import com.manta.worldcup.helper.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AddCookiesInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //리퀘스트빌더
        val builder: Request.Builder = chain.request().newBuilder()
        //저장된 쿠키를 가져온다.
        val pref = context.getSharedPreferences(Constants.PREF_FILENAME_COOKIE, Context.MODE_PRIVATE)
        val cookies = pref.getStringSet(Constants.PREF_COOKIE, HashSet())
        //리퀘스트에 쿠키를 쓴다.
        for (cookie in cookies!!) {
            builder.addHeader("Cookie", cookie)
        }

        //

        return chain.proceed(builder.build())
    }
}