package com.manta.worldcup.activity.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.OnTopicClickDialog
import com.manta.worldcup.adapter.TopicAdapter2
import com.manta.worldcup.adapter.TopicAdapter3
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.UserViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.frag_home.*
import java.util.*
import java.util.Arrays.copyOf
import kotlin.collections.ArrayList
import kotlin.math.min


/**
 * by 변성욱
 * 전체 토픽을 보여주는 프래그먼트
 */
class HomeFragment : Fragment(R.layout.frag_home) {
    private lateinit var mTopicViewModel: TopicViewModel;
    private lateinit var mUserViewModel: UserViewModel;
    private lateinit var mTopicAdaptorTop10Recommend: TopicAdapter2;
    private lateinit var mTopicAdaptorTop10View: TopicAdapter2;
    private lateinit var mTopicAdaptor: TopicAdapter3;

    private val REQUST_ADD_TOPIC = 0;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity == null) return;
        if (context == null) return;

        mTopicAdaptorTop10Recommend = TopicAdapter2(context!!);
        mTopicAdaptorTop10View = TopicAdapter2(context!!);


        mTopicViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(requireActivity().application) as T;
            }
        }).get(TopicViewModel::class.java);

        mUserViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);


        rv_top10_recommend.adapter = mTopicAdaptorTop10Recommend;
        rv_top10_recommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        rv_top10_view.adapter = mTopicAdaptorTop10View;
        rv_top10_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)



        mTopicViewModel.mDataset.observe(this, Observer { topics ->
            val copyList = topics.toMutableList(); //sort하려면 mutableList여야하는듯
            copyList.sortByDescending { it.mLike }
            //subList는 새로운 List를 만드는게 아닌 것 같다. toMutableList로 새로운 리스트를 만들어야함.
            mTopicAdaptorTop10Recommend.setTopics(copyList.subList(0, min(10, copyList.size)).toMutableList())
            copyList.sortByDescending { it.mView }
            mTopicAdaptorTop10View.setTopics(copyList.subList(0, min(10, copyList.size)).toMutableList())
        })



        mTopicAdaptor = TopicAdapter3(context!!);


        rv_all_topic.adapter = mTopicAdaptor;
        rv_all_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mTopicViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(activity!!.application) as T;
            }
        }).get(TopicViewModel::class.java);

        mUserViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);



        mTopicViewModel.mDataset.observe(this, Observer { topics ->
            val copyList = ArrayList(topics)
            mTopicAdaptor.setTopics(copyList);
            //refresh_topic.isRefreshing = false;
        })

        val onTopicClick = onTopicClick@ fun(topicJoinUser: TopicJoinUser) {
            if (mUserViewModel.mUser.value == null) {
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                return@onTopicClick;
            }
            fragmentManager?.let { OnTopicClickDialog().newInstance(topicJoinUser.getTopic(), mUserViewModel.mUser.value!!).show(it, null) };
        }

        //토픽 클릭시 게임 or 선수출진 다이어로그 띄우기
        mTopicAdaptor.setOnItemClickListener(object : TopicAdapter3.OnItemClickListener {
            override fun onItemClick(topicJoinUser: TopicJoinUser) {
                onTopicClick(topicJoinUser)
            }
        })

        mTopicAdaptorTop10Recommend.setOnItemClickListener(
            object : TopicAdapter2.OnItemClickListener {
                override fun onItemClick(topicJoinUser: TopicJoinUser) {
                    onTopicClick(topicJoinUser)
                }
            })


        mTopicAdaptorTop10View.setOnItemClickListener(
            object : TopicAdapter2.OnItemClickListener {
                override fun onItemClick(topicJoinUser: TopicJoinUser) {
                    onTopicClick(topicJoinUser)
                }
            })

    }


}