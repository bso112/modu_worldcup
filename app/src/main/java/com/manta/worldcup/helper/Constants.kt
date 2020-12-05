package com.manta.worldcup.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

object Constants {
    const val BASE_URL = "http://192.168.219.101:8001/anime/";
    const val EXTRA_TOPIC_ID =  "com.manta.worldcup.EXTRA_TOPIC_ID"
    const val EXTRA_PICTURE_NAMES =  "com.manta.worldcup.EXTRA_PICTURE_NAMES"
    const val EXTRA_SUBMIT_LISTENER =  "com.manta.worldcup.EXTRA_SUBMIT_LISTENER"
    const val EXTRA_TOPICMODEL = "com.manta.worldcup.EXTRA_TOPIC"
    const val EXTRA_PICTUREMODEL = "com.manta.worldcup.EXTRA_PICTUREMODEL"
    const val PREF_FILENAME_COOKIE = "com.manta.worldcup.PREF_FILENAME_COOKIE"
    const val PREF_COOKIE = "com.manta.worldcup.PREF_COOKIE"

    /**
     * Glide를 사용해 이미지를 가져와 이미지뷰에 셋팅한다. 헤더에 쿠키를 쓴다.
     * @param url 이미지를 가져올 uri
     * @param view Activity나 Fragment를 찾을 뷰
     * @param imgeView 이미지를 셋팅할 뷰
     * @param context SharedPreferences를 가져올 컨텍스트
     */
    fun GlideWithHeader(url : String, view : View, imgeView : ImageView, context: Context){
        val pref = context.getSharedPreferences(Constants.PREF_FILENAME_COOKIE, Context.MODE_PRIVATE)
        val cookies = pref.getStringSet(Constants.PREF_COOKIE, HashSet())
        val builder = LazyHeaders.Builder();
        //리퀘스트에 쿠키를 쓴다.
        for (cookie in cookies!!) {
            builder.addHeader("Cookie", cookie)
        }
        Glide.with(view).load(GlideUrl(url, builder.build())).into(imgeView);
    }

    fun GlideWithHeader(url : String, fragmentActivity : Activity, imgeView : ImageView, context: Context){
        val pref = context.getSharedPreferences(Constants.PREF_FILENAME_COOKIE, Context.MODE_PRIVATE)
        val cookies = pref.getStringSet(Constants.PREF_COOKIE, HashSet())
        val builder = LazyHeaders.Builder();
        //리퀘스트에 쿠키를 쓴다.
        for (cookie in cookies!!) {
            builder.addHeader("Cookie", cookie)
        }
        Glide.with(fragmentActivity).load(GlideUrl(url, builder.build())).into(imgeView);
    }


}