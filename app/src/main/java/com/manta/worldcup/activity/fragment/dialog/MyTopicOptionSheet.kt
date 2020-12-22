package com.manta.worldcup.activity.fragment.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.activity.UpdateTopicActivity
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.dialog_mytopic_option.*


class MyTopicOptionSheet: BottomSheetDialogFragment() {

    private val mTopicViewModel: TopicViewModel by lazy{
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(activity!!.application) as T;
            }
        }).get(TopicViewModel::class.java);
    };

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_mytopic_option, container, false);
    }

    fun newInstance(topicJoinUser : TopicJoinUser): MyTopicOptionSheet{
        val args = Bundle(1)
        args.putSerializable(Constants.EXTRA_TOPIC_JOIN_USER, topicJoinUser)
        val fragment = MyTopicOptionSheet()
        fragment.arguments = args
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topicJoinUser = arguments?.getSerializable(Constants.EXTRA_TOPIC_JOIN_USER) as? TopicJoinUser ?: return;

        btn_cancel.setOnClickListener {
            dismiss()
        }
        btn_modify.setOnClickListener {
            Intent(context, UpdateTopicActivity::class.java).apply {
                putExtra(Constants.EXTRA_TOPIC, topicJoinUser.getTopic())
                putExtra(Constants.EXTRA_USER, topicJoinUser.getUser())
                startActivity(this)
            }
        }
        btn_delete.setOnClickListener {
            mTopicViewModel.deleteTopic(topicJoinUser.mId);
        }
    }
}