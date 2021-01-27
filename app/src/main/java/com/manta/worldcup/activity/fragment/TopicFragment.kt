package com.manta.worldcup.activity.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.AddTopicActivity
import com.manta.worldcup.activity.LoginActivity
import com.manta.worldcup.activity.fragment.dialog.OnTopicClickDialog
import com.manta.worldcup.adapter.TopicAdpater
import com.manta.worldcup.helper.AuthSingleton
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.TopicViewModel
import com.manta.worldcup.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.frag_topic.*
import kotlin.math.abs

/**
 * 다른 사람의 토픽을 볼 수 있는 프래그먼트
 */
class TopicFragment : Fragment(R.layout.frag_topic){
    private val mTopicViewModel: TopicViewModel by lazy{
        TopicViewModel.provideViewModel(requireActivity(), requireActivity().application)
    };
    private val mUserViewModel: UserViewModel by lazy {
        UserViewModel.provideViewModel(requireActivity(), requireActivity().application);
    };
    private lateinit var mTopicAdaptor: TopicAdpater;

    private val REQUST_ADD_TOPIC = 0;

    private val mRefreshReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            refresh()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity == null) return;
        if (context == null) return;

        mTopicAdaptor = TopicAdpater();

        //당겨서 토픽 리프레쉬
        refresh_topic.setOnRefreshListener {
            mTopicViewModel.getAllTopic();
        }

        rv_topic.adapter = mTopicAdaptor;
        rv_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        mTopicViewModel.mDataset.observe(this, Observer {topics->
            if(topics.isEmpty())
                tv_emty_page.visibility = View.VISIBLE
            else
                tv_emty_page.visibility = View.GONE

            val copyList = ArrayList(topics)
            mTopicAdaptor.setTopics(copyList);
            refresh_topic.isRefreshing = false;
        })

        //토픽 클릭시 게임 or 선수출진 다이어로그 띄우기
        mTopicAdaptor.setOnItemClickListener(object : TopicAdpater.OnItemClickListener {
            override fun onItemClick(topicJoinUser : TopicJoinUser) {
                if(mUserViewModel.mUser.value == null){
                    Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                    return;
                }
                 OnTopicClickDialog()
                     .newInstance(topicJoinUser.getTopic(), mUserViewModel.mUser.value!!)
                     .show(requireFragmentManager(), null) ;
            }
        })

        //토픽버튼 눌렀을때
        btn_add_topic.setOnClickListener {
            AuthSingleton.getInstance(activity!!.application).CheckUserSignIn(
                //로그인이 되어있다면 토픽추가 액티비티로 간다.
                { user ->
                    Constants.showSimpleAlert(context, null, resources.getString(R.string.alert_add_topic), {
                        if (user.mCurrPoint < abs(Constants.POINT_ADD_TOPIC)){
                            Toast.makeText(context, resources.getString(R.string.warn_not_enough_point), Toast.LENGTH_SHORT).show();
                        }else{
                            Intent(context, AddTopicActivity::class.java).apply {
                                putExtra(Constants.EXTRA_USER, user)
                                startActivityForResult(this, REQUST_ADD_TOPIC);
                            }
                        }})
                },
                //로그인 안되어있으면 로그인 액티비티로 간다.
                { user ->
                    Intent(context, LoginActivity::class.java).apply {
                        startActivity(this);
                    }
                })
        }

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(mRefreshReceiver, IntentFilter(Constants.ACTION_NEED_REFRESH))

    }

    private fun refresh(){
        mTopicViewModel.getAllTopic()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mRefreshReceiver);
        super.onDestroy()

    }




}