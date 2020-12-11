package com.manta.worldcup.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.activity.GameResultActivity
import com.manta.worldcup.activity.StatisticActivity
import com.manta.worldcup.adapter.TopicAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.MainViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_my_topic.*


class MyTopicFragment : Fragment(R.layout.fragment_my_topic) {

    private lateinit var mMainViewModel: MainViewModel;
    private lateinit var mTopicAdapter: TopicAdapter
    private lateinit var mTopicViewModel: TopicViewModel;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTopicViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(requireActivity().application) as T;
            }

        }).get(TopicViewModel::class.java);

        mMainViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(requireActivity().application) as T;
            }

        }).get(MainViewModel::class.java);

        context?.let {fragment->
            mTopicAdapter = TopicAdapter(fragment);
            rv_topic.adapter = mTopicAdapter;
            rv_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            mTopicViewModel.mTopics.observe(this, Observer { topic -> mTopicAdapter.setTopics(topic) })
        }

        mTopicAdapter.setOnItemClickListener(object : TopicAdapter.OnItemClickListener{
            override fun onItemClick(topicModel: TopicModel) {
                Intent(context, StatisticActivity::class.java).apply{
                    putExtra(Constants.EXTRA_TOPIC_ID, topicModel.mId);
                    startActivity(this);
                }
            }

        })

    }




    override fun onStart() {
        super.onStart()
        mMainViewModel.mUser.value?.mEmail?.let { mTopicViewModel.getTopics(it) };
    }
}