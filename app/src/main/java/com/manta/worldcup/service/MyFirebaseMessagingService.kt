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

/**
 * FCM을 받기위한 서비스
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {


    private var mNotificationCnt = 0;
    private val NOTIFICATION_GROUP_COMMENT = "comment"


    private val mNotificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    };

    //파이어베이스 메시징을 위해 사용되는 앱 인스턴스용 등록 토큰이 변경될때마다 호출된다.
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(Constants.LOG_TAG, p0);

    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(mNotificationManager)

    }

    /**
     *  웹서버에서 FCM을 요청하고, 파이어베이스에서 이 앱 인스턴스에 FCM을 보내면 호출된다.
     *  @param p0 웹서버에서 FCM 요청과 함께보낸 데이터.
     */
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*
            * p0에는 notification 객체와 메시지에 포함된 데이터(웹서버측에서 보낸 데이터다)가 있다.
            * 이것들을 가지고 노티피케이션을 수행하자.
            */
            notifyNotification(
                p0.notification?.title, /*노티피케이션의 타이틀*/
                p0.notification?.body, /*노티피케이션의 내용*/
                p0.data["topic_id"] ?: "0", /*메시지의 데이터*/
                p0.data["picture_id"] ?: "0"
            );
        }
    }


    /**
     * 실제 노티피케이션을 실행한다.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun notifyNotification(title: String?, content: String?, notifiedTopicId: String, notifiedPictureId: String) {
        if (title == null || content == null) return;

        //알림을 받은 topicId나 pictureID를 로컬에 저장한다.
        SaveNotification(notifiedTopicId, notifiedPictureId)

        //알림을 클릭했을때 알림이 대상이되는 토픽이나 사진으로 이동하게한는 인텐트.
        //인텐트 안에는 알림의 대상의 id를 담는다.
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
            .setAutoCancel(true) //사용자가 스와이프했을때 노티피케이션을 지울것인가
            .setSmallIcon(R.drawable.icon_hestia) //노티피케이션 아이콘
            .setContentTitle(title) //노티피케이션 제목
            .setContentText(content) //노티피케이션 본문
            .setContentIntent(pendingIntent) //노티피케이션을 클릭했을때 실행할 팬딩인텐트
            .setGroup(NOTIFICATION_GROUP_COMMENT) //그룹
            .build()

        //각각의 노티피케이션은 id가 달라야 별개의 노티피케이션으로 인식된다.
        mNotificationManager.notify(Constants.NOTIFICATION_ID + mNotificationCnt++, notification)


        //노티피케이션을 그룹짓는 노티피케이션
        val notificationSummary = Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.icon_hestia)
            .setContentIntent(pendingIntent)
            .setGroup(NOTIFICATION_GROUP_COMMENT)
            .setGroupSummary(true) //이 노티피케이션은 그룹의 부모이다.
            .build()

        //현존하는 노티피케이션 중 NOTIFICATION_GROUP_COMMENT에 해당하는 것들을 그룹으로 묶는다.
        mNotificationManager.notify(Constants.NOTIFICATION_ID_SUMMERY, notificationSummary)
    }

        /**
         * 알림을 받은 topicId나 pictureID를 sharedPreference에 저장한다.
         * 앱을 켰을때, 그동안 온 알림에 대한 정보를 알아야하기 떄문이다.
         * 파라미터 예시) 사진에 댓글이 달렸을경우 notifiedTPictureId는 "0" 이 아니고,
         * notifiedTopicId 는 "0" 이다.
         * @param notifiedPictureId 알림의 대상이되는 사진의 id
         * @param notifiedTopicId 알림의 대상이되는 토픽의 id
         */
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