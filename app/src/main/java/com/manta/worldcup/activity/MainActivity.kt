package com.manta.worldcup.activity

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.ProfileDialog
import com.manta.worldcup.adapter.MainViewPageAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.TopicViewModel
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ll_profile


class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewPagerAdapter: MainViewPageAdapter;

    private val mTopicViewModel: TopicViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(application) as T;
            }
        }).get(TopicViewModel::class.java);
    }
    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(application) as T;
            }
        }).get(UserViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //notificationBar 클리어
        clearNotificationBar();

        //토픽 받아오기
        mTopicViewModel.getAllTopic()

        //사용자가 notificationBar를 클릭해서 앱을 실행했다면 PendingIntent를 통해 데이터를 받을 수 있음.
        val notifiedTopicId = intent.getStringExtra(Constants.EXTRA_NOTIFIED_TOPIC_ID);
        val notifiedPictureId = intent.getStringExtra(Constants.EXTRA_NOTIFIED_PICTURE_ID);

        mMainViewPagerAdapter = MainViewPageAdapter(supportFragmentManager, lifecycle, notifiedTopicId, notifiedPictureId);
        vp_mainPager.adapter = mMainViewPagerAdapter;
        vp_mainPager.isUserInputEnabled = false;

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    vp_mainPager.currentItem = 0
                }
                R.id.page_all_topic -> {
                    vp_mainPager.currentItem = 1
                }
                R.id.page_my_topic -> {
                    vp_mainPager.currentItem = 2
                }
                R.id.page_my_picture -> {
                    vp_mainPager.currentItem = 3
                }
                else -> return@setOnNavigationItemSelectedListener false;
            }
            return@setOnNavigationItemSelectedListener true;
        }

        //사용자가 누른 알림이 토픽에 관한거면 MyTopicFragment를, 아니면 MyPictureFragment를 보여줌
        if (notifiedTopicId != null)
            vp_mainPager.currentItem = 2
        else if (notifiedPictureId != null)
            vp_mainPager.currentItem = 3



        btn_login_logout.setOnClickListener {
            if (btn_login_logout.text == resources.getString(R.string.signout)) {
                //토큰지우기
                val pref = this.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
                pref.edit().putString(Constants.PREF_TOKEN, "").apply();
                SignOut();
            } else {
                Intent(this, LoginActivity::class.java).apply {
                    startActivity(this);
                }
            }
        }

        //내 프로필 정보보기
        iv_profile.setOnClickListener {
            mUserViewModel.mUser.value?.let {
                val profileDialog = ProfileDialog.newInstance(it)
                profileDialog.show(supportFragmentManager, null)
            }
        }

    }


    override fun onStart() {
        super.onStart()
        updateUser()

    }

    fun updateUser(){
        //로그인 상태에따라 로그인, 로그아웃표시
        mUserViewModel.CheckUserSignIn({
            //로그인한 상태이고, 로그인 정보가 변경되었을때만 불림 (최초)
            SignIn(it)
            //로그인한 시점에 받아온 유저정보를 바탕으로 뷰를 갱신해야하기 때문에 이벤트를 브로드캐스트한다.
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply { action = Constants.ACTION_NEED_REFRESH })
        }, {
            //로그아웃 되었을때
            SignOut()
        })

    }


    private fun SignOut() {
        btn_login_logout.text = "로그인"
        ll_profile.visibility = View.INVISIBLE;
        tv_user_nickname.text = "";
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this);
        }
    }

    private fun SignIn(user: User) {
        //로그인됬을때
        setUserInfo(user)

        //파이어베이스 클라우드 메시징을 위한 토큰 요청
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(Constants.LOG_TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            //웹서버에 토큰 등록
            mUserViewModel.registerFirebaseToken(user.mEmail, token!!);
        })


    }

    private fun setUserInfo(user: User) {
        btn_login_logout.text = resources.getString(R.string.signout)
        tv_user_nickname.text = user.mNickname;
        ll_profile.visibility = View.VISIBLE;
        //유저 프로필이미지 셋팅
        if (mUserViewModel.mUser.value != null) {
            val mProfileImgName = mUserViewModel.mUser.value!!.mProfileImgName
            if (mProfileImgName == null) {
                iv_profile.setImageResource(R.drawable.ic_baseline_account_circle_24)
            } else {
                val url = Constants.BASE_URL + "profile_image/get/" + mProfileImgName
                Constants.GlideWithHeader(url, iv_profile, iv_profile, this);
            }
        }
    }

    private fun clearNotificationBar() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}