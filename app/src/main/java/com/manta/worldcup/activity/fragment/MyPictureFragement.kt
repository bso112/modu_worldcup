package com.manta.worldcup.activity.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.MyPictureAdapter
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.frag_mypicture.*

class MyPictureFragement : Fragment(R.layout.frag_mypicture) {
    private lateinit var mUserViewModel: UserViewModel;
    private val myPictureAdapter: MyPictureAdapter = MyPictureAdapter();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);



        rv_picture.layoutManager = GridLayoutManager(context, 2)

        rv_picture.adapter = myPictureAdapter;
        //rv_picture.addItemDecoration(SpaceItemDecoration(80))

        mUserViewModel.mPictures.observe(this, Observer {
            myPictureAdapter.setPictures(ArrayList(it));
        })

    }

    override fun onStart() {
        super.onStart()

        mUserViewModel.getAllPicture()

    }

}