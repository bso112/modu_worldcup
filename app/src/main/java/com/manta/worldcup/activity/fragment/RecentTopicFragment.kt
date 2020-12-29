package com.manta.worldcup.activity.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.OnTopicClickDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.item_topic_recent.*

class RecentTopicFragment : Fragment(R.layout.item_topic_recent) {

    private val mUserViewModel : UserViewModel by lazy{
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);
    }

    fun newInstance(topicJoinUser: TopicJoinUser): RecentTopicFragment{
        val args = Bundle(1)
        args.putSerializable(Constants.EXTRA_TOPIC_JOIN_USER, topicJoinUser)
        val fragment = RecentTopicFragment()
        fragment.arguments = args
        return fragment
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topicJoinUser = requireArguments().getSerializable(Constants.EXTRA_TOPIC_JOIN_USER) as? TopicJoinUser ?: return;

        view.setOnClickListener {
            if (mUserViewModel.mUser.value == null) {
                Toast.makeText(context, resources.getString(R.string.need_signin), Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }
            fragmentManager?.let { OnTopicClickDialog().newInstance(topicJoinUser.getTopic(), mUserViewModel.mUser.value!!).show(it, null) };
        }


        val urlToPicture = Constants.BASE_URL + "image/get/${topicJoinUser.mId}/0";
        Constants.GlideWithHeader(urlToPicture, view, iv_thumbnail, requireContext());


        tv_title.text = topicJoinUser.mTitle;
        tv_managerName.text = topicJoinUser.mManagerName;

        val tierIconID = Constants.getTierIconID(topicJoinUser.mTier);
        if (tierIconID != null)
            iv_tier.setImageResource(tierIconID);
    }
}