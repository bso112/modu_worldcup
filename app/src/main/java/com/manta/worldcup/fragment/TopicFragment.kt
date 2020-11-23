package com.manta.worldcup.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddTopicActivity
import com.manta.worldcup.adapter.TopicAdapter
import com.manta.worldcup.model.Topic
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.frag_topic.*

/**
 * by 변성욱
 * 전체 토픽을 보여주는 프래그먼트
 */
class TopicFragment : Fragment() {
    val mViewModel : TopicViewModel by lazy{
        ViewModelProvider(this).get(TopicViewModel::class.java)
    }

    val mTopicAdaptor : TopicAdapter = TopicAdapter();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_topic, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토픽받아오기
        mViewModel.getAllTopics();

        //당겨서 토픽 리프레쉬
        refresh_topic.setOnRefreshListener { mViewModel.getAllTopics(); }

        rv_topic.adapter = mTopicAdaptor;
        rv_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //토픽 클릭시 토픽생성 액티비티 띄우기
        mTopicAdaptor.setOnItemClickListener(object : TopicAdapter.OnItemClickListener{
            override fun onItemClick(note: Topic) {
                Intent(context, AddTopicActivity::class.java).apply {
                    startActivity(this)
                }
            }
        })

        btn_add_topic.setOnClickListener {
            Intent(context, AddTopicActivity::class.java).apply {
                startActivity(this);
            }
        }

        mViewModel.mTopics.observe(this, Observer {res->
            if(res.isSuccessful) res.body()?.let { mTopicAdaptor.setTopics(it); }
            else Log.d(javaClass.toString(), res.errorBody().toString());
        })
    }

}