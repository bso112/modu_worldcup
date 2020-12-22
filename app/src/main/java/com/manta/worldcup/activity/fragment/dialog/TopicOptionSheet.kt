package com.manta.worldcup.activity.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manta.worldcup.R
import kotlinx.android.synthetic.main.dialog_topic_option.*

class TopicOptionSheet: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_topic_option, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_cancel.setOnClickListener {
            dismiss()
        }
        btn_subscribe.setOnClickListener {

        }
        btn_report.setOnClickListener {

        }
    }
}