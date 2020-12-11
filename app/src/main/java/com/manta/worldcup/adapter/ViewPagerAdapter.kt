package com.manta.worldcup.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.manta.worldcup.activity.fragment.MyPictureFragement
import com.manta.worldcup.activity.fragment.MyTopicFragment
import com.manta.worldcup.activity.fragment.TopicFragment


class ViewPageAdapter(fm : FragmentManager, lifecycle : Lifecycle) : FragmentStateAdapter(fm, lifecycle){


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TopicFragment()
            1 -> MyTopicFragment();
            2 -> MyPictureFragement()
            else -> Fragment() //이 경우 어떻게 되는거지?
        }
    }


}