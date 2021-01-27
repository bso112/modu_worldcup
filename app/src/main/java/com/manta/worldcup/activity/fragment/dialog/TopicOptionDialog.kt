package com.manta.worldcup.activity.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.dialog_topic_option.*

/**
 * 다른 사람의 토픽을 구독, 신고할 수 있는 옵션을 띄워주는 다이어로그다.
 */
class TopicOptionDialog: BottomSheetDialogFragment() {

    private val mTopicViewModel : TopicViewModel by lazy{
        TopicViewModel.provideViewModel(this, requireActivity().application)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_topic_option, container, false);
    }

    fun newInstance(topicJoinUser: TopicJoinUser): TopicOptionDialog{
        val args = Bundle(1)
        args.putSerializable(Constants.EXTRA_TOPIC_JOIN_USER, topicJoinUser)
        val fragment = TopicOptionDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topicJoinUser = requireArguments().getSerializable(Constants.EXTRA_TOPIC_JOIN_USER) as TopicJoinUser? ?: return;

        btn_report.setOnClickListener {
            mTopicViewModel.reportTopic(topicJoinUser)
            Toast.makeText(requireContext(), resources.getString(R.string.report_completed), Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}