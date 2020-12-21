package com.manta.worldcup.activity.fragment.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddPictureActivity
import com.manta.worldcup.activity.GameActivity
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.helper.Constants.EXTRA_TOPICMODEL
import com.manta.worldcup.helper.Constants.EXTRA_TOPIC_ID
import com.manta.worldcup.helper.Constants.EXTRA_USER
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.dialog_ontopicclick.view.*

class OnTopicClickDialog() : DialogFragment() {


    private val mViewModel: UserViewModel by lazy {
       ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }
        }).get(UserViewModel::class.java);
    };

    fun newInstance(topic : Topic, user : User) : OnTopicClickDialog{
        val args = Bundle(1)
        args.putSerializable(EXTRA_TOPICMODEL, topic);
        args.putSerializable(EXTRA_USER, user);
        val fragment = OnTopicClickDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_ontopicclick, container, false);

        val topicModel = arguments?.getSerializable(EXTRA_TOPICMODEL) as? Topic ?: return view;
        val user = arguments?.getSerializable(EXTRA_USER) as? User ?: return view;

        view.tv_topic_title.text = topicModel.mTitle;

        view.tv_topic_description.text = topicModel.mDescription;


        view.btn_game_start.setOnClickListener {
                Intent(context, GameActivity::class.java).apply {
                    putExtra(EXTRA_TOPICMODEL, topicModel)
                    putExtra(EXTRA_USER, user)
                    startActivity(this);
                    dismiss()
                }
            }
        view.btn_add_picture.setOnClickListener {
                Intent(context, AddPictureActivity::class.java).apply {
                    putExtra(EXTRA_TOPIC_ID, topicModel.mId)
                    putExtra(Constants.EXTRA_USER_EMAIL, mViewModel.mUser.value?.mEmail)
                    startActivity(this);
                    dismiss()
                }
            }

        return view;
    }




}