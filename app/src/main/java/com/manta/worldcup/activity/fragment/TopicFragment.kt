package com.manta.worldcup.activity.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddTopicActivity
import com.manta.worldcup.activity.LoginActivity
import com.manta.worldcup.activity.fragment.dialog.OnTopicClickDialog
import com.manta.worldcup.adapter.TopicAdapter
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.AuthSingleton
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.frag_topic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * by 변성욱
 * 전체 토픽을 보여주는 프래그먼트
 */
class TopicFragment : Fragment(R.layout.frag_topic) {
    private lateinit var mViewModel: TopicViewModel;
    private lateinit var mTopicAdaptor: TopicAdapter;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity == null) return;
        if (context == null) return;

        mTopicAdaptor = TopicAdapter(context!!);

        mViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(activity!!.application) as T;
            }

        }).get(TopicViewModel::class.java);


        //토픽받아오기
        mViewModel.getAllTopics();

        //당겨서 토픽 리프레쉬
        refresh_topic.setOnRefreshListener { mViewModel.getAllTopics(); }

        rv_topic.adapter = mTopicAdaptor;
        rv_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //토픽 클릭시 게임 or 선수출진 다이어로그 띄우기
        mTopicAdaptor.setOnItemClickListener(object : TopicAdapter.OnItemClickListener {
            override fun onItemClick(topicModel: TopicModel) {
                fragmentManager?.let { OnTopicClickDialog().newInstance(topicModel).show(it, null) };
            }
        })

        btn_add_topic.setOnClickListener {
                AuthSingleton.getInstance(activity!!.application).CheckUserSignIn({
                    Intent(context, AddTopicActivity::class.java).apply {
                        startActivity(this);
                    }}, {
                    Intent(context, LoginActivity::class.java).apply {
                        startActivity(this);
                    }})
        }

        mViewModel.mTopics.observe(this, Observer {
            mTopicAdaptor.setTopics(it);
            refresh_topic.isRefreshing = false;
        })
    }

}