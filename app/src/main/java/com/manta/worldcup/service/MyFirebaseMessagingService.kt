package com.manta.worldcup.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.manta.worldcup.R
import com.manta.worldcup.activity.MainActivity
import com.manta.worldcup.activity.fragment.MyTopicFragment
import com.manta.worldcup.helper.Constants


class MyFirebaseMessagingService : FirebaseMessagingService() {


    private var mNotificationCnt = 0;
    private val NOTIFICATION_GROUP_COMMENT = "comment"


    private val mNotificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    };


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
            notifyNotification(
                p0.notification?.title,
                p0.notification?.body,
                p0.data["topic_id"] ?: "0",
                p0.data["picture_id"] ?: "0"
            );
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun notifyNotification(title: String?, content: String?, notifiedTopicId: String, notifiedPictureId: String) {
        if (title == null || content == null) return;

        //알림을 받은 topicId나 pictureID를 로컬에 저장한다.
        SaveNotification(notifiedTopicId, notifiedPictureId)

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            if (notifiedTopicId != "0")
                putExtra(Constants.EXTRA_NOTIFIED_TOPIC_ID, notifiedTopicId)
            else if (notifiedPictureId != "0")
                putExtra(Constants.EXTRA_NOTIFIED_PICTURE_ID, notifiedPictureId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, FLAG_UPDATE_CURRENT
        )


        val notification = Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.google)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setGroup(NOTIFICATION_GROUP_COMMENT)
            .build()

        mNotificationManager.notify(Constants.NOTIFICATION_ID + mNotificationCnt++, notification)


        //노티피케이션을 그룹짓는 노티피케이션
        val notificationSummary = Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.google)
            .setContentIntent(pendingIntent)
            .setGroup(NOTIFICATION_GROUP_COMMENT)
            .setGroupSummary(true)
            .build()

        //notify시의 id가 같다면 이미 있는걸 갱신한다.

        mNotificationManager.notify(Constants.NOTIFICATION_ID_SUMMERY, notificationSummary)
    }

    //알림을 받은 topicId나 pictureID를 로컬에 저장한다.
    private fun SaveNotification(notifiedTopicId: String, notifiedPictureId: String) {
        if (!notifiedTopicId.equals("0") || !notifiedPictureId.equals("0")) {
            val sharedPref = applicationContext.getSharedPreferences(Constants.PREF_FILE_NOTIFICATION, Context.MODE_PRIVATE)
            val prefEdit = sharedPref.edit();
            var notifiedPictureIDs: MutableSet<String>?
            var notifiedTopicIDs: MutableSet<String>?

            if (!notifiedTopicId.equals("0")) {
                notifiedTopicIDs = sharedPref.getStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, HashSet());
                notifiedTopicIDs?.add(notifiedTopicId);
                prefEdit.putStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, notifiedTopicIDs);
            }
            if (!notifiedPictureId.equals("0")) {
                notifiedPictureIDs = sharedPref.getStringSet(Constants.PREF_NOTIFIED_PICTURE_ID, HashSet());
                notifiedPictureIDs?.add(notifiedPictureId);
                prefEdit.putStringSet(Constants.PREF_NOTIFIED_PICTURE_ID, notifiedPictureIDs)
            }
            prefEdit.apply();
        }

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