package com.manta.worldcup.activity.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manta.worldcup.R
import kotlinx.android.synthetic.main.dialog_topic_option.*

/**
 * 다른 사람의 토픽을 구독, 신고할 수 있는 옵션을 띄워주는 다이어로그다.
 */
class TopicOptionDialog: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_topic_option, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_subscribe.setOnClickListener {

        }
        btn_report.setOnClickListener {

        }
    }
}