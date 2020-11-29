package com.manta.worldcup.helper

import android.util.Log
import java.lang.Exception

object Constants {
    const val BASE_URL = "http://192.168.219.101:8001/anime/";
    const val EXTRA_TOPIC_ID =  "com.manta.worldcup.EXTRA_TOPIC_ID"
    const val EXTRA_PICTURE_NAMES =  "com.manta.worldcup.EXTRA_PICTURE_NAMES"
    const val EXTRA_SUBMIT_LISTENER =  "com.manta.worldcup.EXTRA_SUBMIT_LISTENER"

    /**
     * by 변성욱
     * 블록에서 실행되는 모든 익셉션은 로그로 처리된다.
     */
    fun tryCatchLog(function : ()->Unit){
        try {
            function();
        }catch (exception : Exception){
            exception.message?.let { Log.d("Exception", it) }
        }
    }

    /**
     * by 변성욱
     * 블록에서 실행되는 모든 익셉션은 로그로 처리된다.
     */
     suspend fun tryCatchLogSuspend(function : suspend ()->Unit){
        try {
            function();
        }catch (exception : Exception){
            exception.message?.let { Log.d("Exception", it) }
        }
    }

}