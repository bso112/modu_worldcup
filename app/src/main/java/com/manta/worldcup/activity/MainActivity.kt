package com.manta.worldcup.activity

import android.app.NotificationManager
import android.content.Context
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
import com.manta.worldcup.activity.fragment.dialog.ChangeNicknameDialog
import com.manta.worldcup.adapter.MainViewPageAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.TopicViewModel
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ll_point
import kotlinx.android.synthetic.main.activity_main.tv_point


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

        //notification 클리어
        clearNotification();

        //토픽 받아오기
        mTopicViewModel.getAllTopic()


        mMainViewPagerAdapter = MainViewPageAdapter(supportFragmentManager, lifecycle);
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


        btn_login_logout.setOnClickListener {
            if (btn_login_logout.text == "로그아웃") {
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

        tv_user_nickname.setOnClickListener {
            ChangeNicknameDialog().newInstance(object : ChangeNicknameDialog.OnSubmitListener {
                override fun onSubmit(nickname: String) {
                    mUserViewModel.updateUserNickname(nickname) {
                        tv_user_nickname.text = nickname;
                        //변경사항 반영
                        mTopicViewModel.getAllTopic();
                    };
                }
            }).show(supportFragmentManager, null);
        }


    }


    override fun onStart() {
        super.onStart()
        //모든 토픽을 받아온다. 이 토픽은 HomeFragment가 참조한다.
        mTopicViewModel.getAllTopic()
        //로그인 상태에따라 로그인, 로그아웃표시
        mUserViewModel.CheckUserSignIn({
            SignIn(it)
            //로그인한 시점에 받아온 유저정보를 바탕으로 뷰를 갱신해야하기 때문에 이벤트를 브로드캐스트한다.
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply { action = Constants.ACTION_SIGNIN })
        }, {
            //로그아웃 되었을때
            SignOut()
        })

    }


    private fun SignOut() {
        btn_login_logout.text = "로그인"
        ll_point.visibility = View.INVISIBLE;
        iv_tier.visibility = View.INVISIBLE;
        tv_user_nickname.text = "";
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this);
        }
    }

    private fun SignIn(user: User) {
        //로그인됬을때
        btn_login_logout.text = "로그아웃";
        tv_user_nickname.text = user.mNickname;
        ll_point.visibility = View.VISIBLE;
        tv_point.text = user.mCurrPoint.toString();
        val tier = Constants.getTierIconID(user.mTier);
        tier?.let { _tier -> iv_tier.setImageResource(_tier) };
        iv_tier.visibility = View.VISIBLE;

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


    fun clearNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }


}