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
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.helper.Constants.EXTRA_TOPIC_ID
import kotlinx.android.synthetic.main.dialog_ontopicclick.view.*

class OnTopicClickDialog() : DialogFragment() {

    fun newInstance(topicId : Long) : OnTopicClickDialog{
        val args = Bundle(1)
        args.putLong(EXTRA_TOPIC_ID, topicId);
        val fragment = OnTopicClickDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_ontopicclick, container, false);

        val topicId = arguments?.getLong(EXTRA_TOPIC_ID);

        view.btn_game_start.setOnClickListener {
                Intent(context, GameActivity::class.java).apply {
                    putExtra(EXTRA_TOPIC_ID, topicId)
                    startActivity(this);
                }
            }
        view.btn_add_picture.setOnClickListener {
                Intent(context, AddPictureActivity::class.java).apply {
                    putExtra(EXTRA_TOPIC_ID, topicId)
                    startActivity(this);
                }
            }

        return view;
    }




}