package com.manta.worldcup.activity.fragment.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddPictureActivity
import com.manta.worldcup.activity.GameActivity
import com.manta.worldcup.helper.Constants.EXTRA_TOPICMODEL
import com.manta.worldcup.helper.Constants.EXTRA_TOPIC_ID
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.TopicModel
import kotlinx.android.synthetic.main.dialog_ontopicclick.view.*

class OnTopicClickDialog() : DialogFragment() {

    fun newInstance(topic : TopicModel) : OnTopicClickDialog{
        val args = Bundle(1)
        args.putSerializable(EXTRA_TOPICMODEL, topic);
        val fragment = OnTopicClickDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_ontopicclick, container, false);

        val topicModel = arguments?.getSerializable(EXTRA_TOPICMODEL) as? TopicModel ?: return view;

        view.tv_topic_title.text = topicModel.mTitle;
        view.tv_topic_description.text = topicModel.mDescription;


        view.btn_game_start.setOnClickListener {
                Intent(context, GameActivity::class.java).apply {
                    putExtra(EXTRA_TOPICMODEL, topicModel)
                    startActivity(this);
                    dismiss()
                }
            }
        view.btn_add_picture.setOnClickListener {
                Intent(context, AddPictureActivity::class.java).apply {
                    putExtra(EXTRA_TOPIC_ID, topicModel.mId)
                    startActivity(this);
                    dismiss()
                }
            }

        return view;
    }




}