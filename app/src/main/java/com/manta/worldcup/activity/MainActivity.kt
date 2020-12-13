package com.manta.worldcup.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.manta.worldcup.R
import com.manta.worldcup.adapter.MainViewPageAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewPagerAdapter: MainViewPageAdapter;
    private val mUserViewModel : UserViewModel by lazy{
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(application) as T;
            }
        }).get(UserViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainViewPagerAdapter = MainViewPageAdapter(supportFragmentManager, lifecycle);
        vp_mainPager.adapter = mMainViewPagerAdapter;

        TabLayoutMediator(tl_mainTab, vp_mainPager) { tab, position ->
            when (position) {
                0 -> { tab.text = "토픽" }
                1 -> { tab.text = "내 토픽" }
                2 -> { tab.text = "내 사진"}
            }
        }.attach()


        btn_login_logout.setOnClickListener {
            if(btn_login_logout.text == "로그아웃"){
                //토큰지우기
                val pref = this.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
                pref.edit().putString(Constants.PREF_TOKEN, "").apply();
                SignOut();
            }else{
                Intent(this, LoginActivity::class.java).apply {
                    startActivity(this);
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //로그인 상태에따라 로그인, 로그아웃표시
        mUserViewModel.CheckUserSignIn({
            SignIn(it)
        }, {
            //로그아웃 되었을때
           SignOut()
        })

    }

    private fun SignOut(){
        btn_login_logout.text = "로그인"
        ll_point.visibility = View.INVISIBLE;
        iv_tier.visibility = View.INVISIBLE;
        et_nickname.text = "";
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this);
        }
    }

    private fun SignIn(user : User){
        //로그인됬을때
        btn_login_logout.text = "로그아웃";
        et_nickname.text = user.mNickname;
        ll_point.visibility = View.VISIBLE;
        tv_point.text = user.mCurrPoint.toString();
        val tier = Constants.getTierIconID(user.mTier);
        tier?.let{ _tier -> iv_tier.setImageResource(_tier)};
        iv_tier.visibility = View.VISIBLE;
    }



}