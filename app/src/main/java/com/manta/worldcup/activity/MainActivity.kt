package com.manta.worldcup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.manta.worldcup.R
import com.manta.worldcup.adapter.ViewPageAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mViewPagerAdapter: ViewPageAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mViewPagerAdapter = ViewPageAdapter(supportFragmentManager, lifecycle);

        vp_mainPager.adapter = mViewPagerAdapter;

        TabLayoutMediator(tl_mainTab, vp_mainPager) { tab, position ->
                when (position) {
                    0 -> { tab.text = "토픽"}
                    1 -> {tab.text = "내 이미지"}
                }
        }.attach()


    }
}