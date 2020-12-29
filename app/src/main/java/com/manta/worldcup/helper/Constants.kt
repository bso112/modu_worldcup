package com.manta.worldcup.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.manta.worldcup.R
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashSet

object Constants {
    const val BASE_URL = "http://192.168.219.144:8001/anime/";
    const val LOG_TAG = "com.manta.worldcup"

    const val EXTRA_TOPIC_ID = "com.manta.worldcup.EXTRA_TOPIC_ID"
    const val EXTRA_NOTIFIED_TOPIC_ID = "com.manta.worldcup.EXTRA_NOTIFIED_TOPIC_ID"
    const val EXTRA_NOTIFIED_PICTURE_ID = "com.manta.worldcup.EXTRA_PICTURE_ID"
    const val EXTRA_PICTURE_NAMES = "com.manta.worldcup.EXTRA_PICTURE_NAMES"
    const val EXTRA_PICTURE_NAME = "com.manta.worldcup.EXTRA_PICTURE_NAME"
    const val EXTRA_SUBMIT_LISTENER = "com.manta.worldcup.EXTRA_SUBMIT_LISTENER"
    const val EXTRA_TOPIC = "com.manta.worldcup.EXTRA_TOPIC"
    const val EXTRA_PICTURE_MODEL = "com.manta.worldcup.EXTRA_PICTURE"
    const val EXTRA_USER_EMAIL =  "com.manta.worldcup.PREF_USER_EMAIL"
    const val EXTRA_USER_NICKNAME =  "com.manta.worldcup.EXTRA_USER_NICKNAME"
    const val EXTRA_USER =  "com.manta.worldcup.EXTRA_USER"
    const val EXTRA_TOPIC_JOIN_USER = "com.manta.worldcup.EXTRA_TOPIC_JOIN_USER"
    const val EXTRA_MYPICTURE_OPTION_CLICK_LISTENER = "com.manta.worldcup.EXTRA_MYPICTURE_OPTION_CLICK_LISTENER"
    const val EXTRA_PICTURE_NAME_CHANGE_LISTENER = "com.manta.worldcup.EXTRA_PICTURE_NAME_CHANGE_LISTENER"
    const val PREF_FILENAME_COOKIE = "com.manta.worldcup.PREF_FILENAME_COOKIE"
    const val PREF_COOKIE = "com.manta.worldcup.PREF_COOKIE"
    const val PREF_FILENAME_TOKEN = "com.manta.worldcup.PREF_FILENAME_TOKEN"
    const val PREF_TOKEN = "com.manta.worldcup.PREF_TOKEN"
    const val PREF_FILE_NOTIFICATION = "com.manta.worldcup.PREF_FILE_NOTIFICATION"
    const val PREF_NOTIFIED_TOPIC_ID = "com.manta.worldcup.PREF_NOTIFIED_TOPIC_ID"
    const val PREF_NOTIFIED_PICTURE_ID = "com.manta.worldcup.PREF_NOTIFIED_PICTURE_ID"

    const val NOTIFICATION_CHANNEL_ID = "worldcup"
    const val NOTIFICATION_CHANNEL_NAME = "worldcup"
    const val NOTIFICATION_ID = 2;
    const val NOTIFICATION_ID_SUMMERY = 1;

    /**
     * 로그인을 하거나, 토픽이나 픽쳐가 삭제되었거나 추가되는 등의 이벤트가 발생할 경우
     * 화면을 리프레시할 필요가 있다. 그것을 위한 액션
     */
    const val ACTION_NEED_REFRESH = "ACTION_NEED_REFRESH"
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
    const val POINT_CLEAR_GAME = 5


    /**
     * Glide를 사용해 이미지를 가져와 이미지뷰에 셋팅한다. 헤더에 쿠키를 쓴다.
     * @param url 이미지를 가져올 uri
     * @param view Activity나 Fragment를 찾을 뷰
     * @param imgeView 이미지를 셋팅할 뷰
     * @param context SharedPreferences를 가져올 컨텍스트
     * @param isUseCache 캐시를 사용하는가?
     */
    fun GlideWithHeader(url: String, view: View, imgeView: ImageView, context: Context, isUseCache : Boolean = true) {
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
        if(isUseCache)
            Glide.with(view).load(GlideUrl(url, builder.build())).into(imgeView);
        else{
            Glide
                .with(view)
                .load(GlideUrl(url, builder.build()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgeView);
        }
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
                setPositiveButton("확인") { _, _ ->
                    onClickOk()
                }
                setNegativeButton("취소") { _, _ ->
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

    fun resolveAttribute(context : Context, attrId : Int) : Int{
        val typedValue = TypedValue();
        context.theme.resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }


    /**
     * lastDate이 현재로부터 얼마나 시간이 지났는지 얻어온다.
     *  @param resources 문자열리소스를 얻어올 리소스
     *  @param lastDate 예전 시간
     */
    fun getTimePassedFromNow(resources : Resources, lastDate : Date) : String{
        val currdate = Calendar.getInstance().time;
        val date = lastDate
        val diffInMillies = currdate.time - date.time;


        val diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        val diffYears = (diffDays / 365).toLong()
        val diffHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        val diffMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if(diffYears > 0)
            return diffYears.toString() + resources.getString(R.string.year_before)
        else if(diffDays > 0)
            return diffDays.toString() + resources.getString(R.string.day_before)
        else if(diffHours > 0)
            return diffHours.toString() + resources.getString(R.string.hour_before)
        else if(diffMinutes > 0)
            return diffMinutes.toString() + resources.getString(R.string.minute_before)

        return resources.getString(R.string.just_before)
    }




}