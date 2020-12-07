package com.manta.worldcup.activity

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.manta.worldcup.R
import com.manta.worldcup.adapter.ViewPageAdapter
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.AuthSingleton
import com.manta.worldcup.helper.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mViewPagerAdapter: ViewPageAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewPagerAdapter = ViewPageAdapter(supportFragmentManager, lifecycle);

        vp_mainPager.adapter = mViewPagerAdapter;

        TabLayoutMediator(tl_mainTab, vp_mainPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "토픽"
                }
                1 -> {
                    tab.text = "내 이미지"
                }
            }
        }.attach()


        btn_login_logout.setOnClickListener {
            if(btn_login_logout.text == "로그아웃"){
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
        AuthSingleton.getInstance(application).CheckUserSignIn({
            btn_login_logout.text = "로그아웃";
            tv_nickname.text = it.mNickname;
        }, { btn_login_logout.text = "로그인";})

    }



}