package com.manta.worldcup.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddTopicActivity
import com.manta.worldcup.activity.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ln

object Constants {
    const val BASE_URL = "http://192.168.219.144:8001/anime/";
    const val LOG_TAG = "com.manta.worldcup"

    const val EXTRA_TOPIC_ID = "com.manta.worldcup.EXTRA_TOPIC_ID"
    const val EXTRA_PICTURE_NAMES = "com.manta.worldcup.EXTRA_PICTURE_NAMES"
    const val EXTRA_SUBMIT_LISTENER = "com.manta.worldcup.EXTRA_SUBMIT_LISTENER"
    const val EXTRA_TOPICMODEL = "com.manta.worldcup.EXTRA_TOPIC"
    const val EXTRA_PICTUREMODEL = "com.manta.worldcup.EXTRA_PICTUREMODEL"
    const val EXTRA_USER_EMAIL =  "com.manta.worldcup.PREF_USER_EMAIL"
    const val EXTRA_USER_NICKNAME =  "com.manta.worldcup.EXTRA_USER_NICKNAME"
    const val EXTRA_USER =  "com.manta.worldcup.EXTRA_USER"

    const val PREF_FILENAME_COOKIE = "com.manta.worldcup.PREF_FILENAME_COOKIE"
    const val PREF_COOKIE = "com.manta.worldcup.PREF_COOKIE"
    const val PREF_FILENAME_TOKEN = "com.manta.worldcup.PREF_FILENAME_TOKEN"
    const val PREF_TOKEN = "com.manta.worldcup.PREF_TOKEN"

    const val NOTIFICATION_CHANNEL_ID = "worldcup"
    const val NOTIFICATION_CHANNEL_NAME = "worldcup"
    const val NOTIFICATION_ID = 2;
    const val NOTIFICATION_ID_SUMMERY = 1;
    /**
     * 토픽생성할때 드는 포인트
     */
    const val POINT_ADD_TOPIC = -100

    /**
     * 사진이 1위를 했을때 사진 소유자가 받는 포인트
     */
    const val POINT_WIN_PICTURE = 20

    /**
     * 토픽의 참여자가 게임을 완료했을때 토픽 주최자가 받는 포인트
     */
    const val POINT_END_GAME = 5


    /**
     * Glide를 사용해 이미지를 가져와 이미지뷰에 셋팅한다. 헤더에 쿠키를 쓴다.
     * @param url 이미지를 가져올 uri
     * @param view Activity나 Fragment를 찾을 뷰
     * @param imgeView 이미지를 셋팅할 뷰
     * @param context SharedPreferences를 가져올 컨텍스트
     */
    fun GlideWithHeader(url: String, view: View, imgeView: ImageView, context: Context) {
        val prefCookies = context.getSharedPreferences(Constants.PREF_FILENAME_COOKIE, Context.MODE_PRIVATE)
        val cookies = prefCookies.getStringSet(Constants.PREF_COOKIE, HashSet())
        val builder = LazyHeaders.Builder();
        //리퀘스트에 쿠키를 쓴다.
        for (cookie in cookies!!) {
            builder.addHeader("Cookie", cookie)
        }
        //토큰을 쓴다.
        val prefToken = context.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
        val token = prefToken.getString(Constants.PREF_TOKEN, "")
        if (token != null && token != "")
            builder.addHeader("Token", token);

        Glide.with(view).load(GlideUrl(url, builder.build())).into(imgeView);
    }

    fun GlideWithHeader(url: String, fragmentActivity: Activity, imgeView: ImageView, context: Context) {
        val pref = context.getSharedPreferences(Constants.PREF_FILENAME_COOKIE, Context.MODE_PRIVATE)
        val cookies = pref.getStringSet(Constants.PREF_COOKIE, HashSet())
        val builder = LazyHeaders.Builder();
        //리퀘스트에 쿠키를 쓴다.
        for (cookie in cookies!!) {
            builder.addHeader("Cookie", cookie)
        }
        //토큰을 쓴다.
        val prefToken = context.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
        val token = prefToken.getString(Constants.PREF_TOKEN, "")
        if (token != null && token != "")
            builder.addHeader("Token", token);
        Glide.with(fragmentActivity).load(GlideUrl(url, builder.build())).into(imgeView);

    }

    /*
    by 변성욱
    간단한 경고 다이어로그를 띄운다.
     */
    fun showSimpleAlert(context: Context?, title: String?, message: String, onClickOk: () -> Unit, onClickCancle: (() -> Unit)? = null) {
        val alertDialog: AlertDialog? = context?.let { context ->
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setPositiveButton("확인") { dialog, which ->
                    onClickOk()
                }
                setNegativeButton("취소") { dialog, which ->
                    if (onClickCancle != null) {
                        onClickCancle()
                    }
                }

                title?.let { setTitle(it) }
                setMessage(message)
            }
            builder.create()
        }

        alertDialog?.show()
    }

    fun getTierIconID(tier : Int) : Int? {
        return when (tier) {
            0 -> R.drawable.ic_tier0_24
            else -> null;
        }
    }





}