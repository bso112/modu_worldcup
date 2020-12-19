package com.manta.worldcup.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.manta.worldcup.activity.fragment.MyPictureFragement
import com.manta.worldcup.activity.fragment.MyTopicFragment
import com.manta.worldcup.activity.fragment.HomeFragment
import com.manta.worldcup.activity.fragment.TopicFragment


class MainViewPageAdapter(fm : FragmentManager, lifecycle : Lifecycle) : FragmentStateAdapter(fm, lifecycle){


    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment();
            1 -> TopicFragment();
            2 -> MyTopicFragment();
            3 -> MyPictureFragement()
            else -> Fragment()
        }
    }


}