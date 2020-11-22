package com.manta.worldcup.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddPictureActivity
import com.manta.worldcup.activity.GameActivity
import kotlinx.android.synthetic.main.dialog_ontopicclick.view.*

class OnTopicClickDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_ontopicclick, container, false);

        view.btn_game_start.setOnClickListener {
                Intent(context, GameActivity::class.java).apply {
                    startActivity(this);
                }
            }
        view.btn_add_picture.setOnClickListener {
                Intent(context, AddPictureActivity::class.java).apply {
                    startActivity(this);
                }
            }

        return view;
    }




}