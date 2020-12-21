package com.manta.worldcup.activity.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.MyPictureAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.frag_mypicture.*

class MyPictureFragement : Fragment(R.layout.frag_mypicture) {
    private lateinit var mUserViewModel: UserViewModel;
    private lateinit var mPictureAdapter: MyPictureAdapter;
    private val mSigninEventReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            refresh()
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);

        mPictureAdapter = MyPictureAdapter(requireActivity().supportFragmentManager)
        rv_picture.layoutManager = GridLayoutManager(context, 2)
        rv_picture.adapter = mPictureAdapter;

        mUserViewModel.mUser.observe(this, Observer {user->
            mPictureAdapter.setUser(user);
        })

        mUserViewModel.mPictures.observe(this, Observer {
            mPictureAdapter.setPictures(ArrayList(it));
        })

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(mSigninEventReceiver, IntentFilter(Constants.ACTION_SIGNIN))

    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mSigninEventReceiver);
        super.onDestroy()

    }

    private fun refresh(){
        //내 사진을 가져온다.
        mUserViewModel.getAllPicture()
    }





}