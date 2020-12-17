package com.manta.worldcup.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.manta.worldcup.R
import com.manta.worldcup.activity.MainActivity
import com.manta.worldcup.helper.Constants


class MyFirebaseMessagingService : FirebaseMessagingService() {


    var mNotificationCnt = 0;
    val NOTIFICATION_GROUP_COMMENT = "comment"
    private val mNotificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    };

    private val mPendingIntent: PendingIntent by lazy {
        val notificationIntent = Intent(this, MainActivity::class.java)
        PendingIntent.getActivity(
            this, 0, notificationIntent, FLAG_UPDATE_CURRENT
        )
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(Constants.LOG_TAG, p0);

    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(mNotificationManager)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifyNotification(p0.notification?.title, p0.notification?.body);
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun notifyNotification(title: String?, content: String?) {
        if (title == null || content == null) return;
        val notification = Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.google)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(mPendingIntent)
            .setGroup(NOTIFICATION_GROUP_COMMENT)
            .build()

        mNotificationManager.notify(Constants.NOTIFICATION_ID + mNotificationCnt++, notification)

        //노티피케이션을 그룹짓는 노티피케이션
        val notificationSummary = Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.google)
            .setContentIntent(mPendingIntent)
            .setGroup(NOTIFICATION_GROUP_COMMENT)
            .setGroupSummary(true)
            .build()

        //notify시의 id가 같다면 이미 있는걸 갱신한다.

        mNotificationManager.notify(Constants.NOTIFICATION_ID_SUMMERY, notificationSummary)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW //LOW로 해줘야 진동안울림
        )

        notificationManager.createNotificationChannel(notificationChannel);
    }
}