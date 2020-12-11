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
import com.manta.worldcup.adapter.ViewPageAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mViewPagerAdapter: ViewPageAdapter;
    private val mMainViewModel : MainViewModel by lazy{
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(application) as T;
            }
        }).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewPagerAdapter = ViewPageAdapter(supportFragmentManager, lifecycle);
        vp_mainPager.adapter = mViewPagerAdapter;

        TabLayoutMediator(tl_mainTab, vp_mainPager) { tab, position ->
            when (position) {
                0 -> { tab.text = "토픽" }
                1 -> { tab.text = "내 토픽" }
                2 -> { tab.text = "내 사진"}
            }
        }.attach()


        btn_login_logout.setOnClickListener {
            if(btn_login_logout.text == "로그아웃"){
                //로그아웃처리
                btn_login_logout.text = "로그인"
                tv_nickname.text = ""
                //토큰지우기
                val pref = this.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
                pref.edit().putString(Constants.PREF_TOKEN, "").apply();
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
        mMainViewModel.CheckUserSignIn({
            //로그인됬을때
            btn_login_logout.text = "로그아웃";
            tv_nickname.text = it.mNickname;
            ll_point.visibility = View.VISIBLE;
            tv_point.text = it.mCurrPoint.toString();
            when(it.mTier){
                0 -> iv_tier.setImageResource(R.drawable.ic_tier0_24)
            }
            iv_tier.visibility = View.VISIBLE;
        }, {
            btn_login_logout.text = "로그인"
            ll_point.visibility = View.INVISIBLE;
            iv_tier.visibility = View.INVISIBLE;
            tv_nickname.text = "";
        })

    }



}