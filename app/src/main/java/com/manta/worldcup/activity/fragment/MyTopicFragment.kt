package com.manta.worldcup.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.StatisticActivity
import com.manta.worldcup.adapter.TopicAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.UserViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_my_topic.*
import kotlinx.coroutines.CoroutineScope


class MyTopicFragment : Fragment(R.layout.fragment_my_topic) {

    private lateinit var mUserViewModel: UserViewModel;
    private lateinit var mTopicAdapter: TopicAdapter
    private lateinit var mTopicViewModel: TopicViewModel;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTopicViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(requireActivity().application) as T;
            }
        }).get(TopicViewModel::class.java);

        mUserViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);

        context?.let {fragment->
            mTopicAdapter = TopicAdapter(fragment);
            rv_topic.adapter = mTopicAdapter;
            rv_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            mTopicViewModel.mDataset.observe(this, Observer { topic -> mTopicAdapter.setTopics(topic) })
        }

        mTopicAdapter.setOnItemClickListener(object : TopicAdapter.OnItemClickListener{
            override fun onItemClick(topicJoinUser: TopicJoinUser) {
                Intent(context, StatisticActivity::class.java).apply{
                    putExtra(Constants.EXTRA_TOPICMODEL, topicJoinUser.getTopic());
                    putExtra(Constants.EXTRA_USER, mUserViewModel.mUser.value);
                    startActivity(this);
                }
            }

        })

    }




    override fun onStart() {
        super.onStart()
        mUserViewModel.mUser.value?.mEmail?.let { mTopicViewModel.getTopics(it) };
    }
}