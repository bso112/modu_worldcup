package com.manta.worldcup.activity.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.StatisticActivity
import com.manta.worldcup.adapter.MyTopicAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.UserViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_my_topic.*


class MyTopicFragment : Fragment(R.layout.fragment_my_topic) {

    private lateinit var mUserViewModel: UserViewModel;
    private lateinit var mTopicAdapter: MyTopicAdapter;
    private lateinit var mTopicViewModel: TopicViewModel;
    private val mSigninEventReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            refresh()
        }
    }

    /**
     * @param notifiedTopicId : notificationBanner를 클릭했을때 전달받은 topicId
     */
    fun newInstance(notifiedTopicId : String?): MyTopicFragment{
        val args = Bundle(1)
        args.putString(Constants.EXTRA_NOTIFIED_TOPIC_ID,notifiedTopicId)
        val fragment = MyTopicFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTopicViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(requireActivity().application) as T;
            }
        }).get(TopicViewModel::class.java);

        mUserViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);


        mTopicAdapter = MyTopicAdapter(arguments?.getString(Constants.EXTRA_NOTIFIED_TOPIC_ID));
        rv_topic.adapter = mTopicAdapter;
        rv_topic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        mTopicViewModel.mDataset.observe(this, Observer { topic ->
            mTopicAdapter.setTopics(topic)
        })


        mTopicAdapter.setOnItemClickListener(object : MyTopicAdapter.OnItemClickListener {
            override fun onItemClick(topicJoinUser: TopicJoinUser) {
                Intent(context, StatisticActivity::class.java).apply {
                    putExtra(Constants.EXTRA_TOPIC, topicJoinUser.getTopic());
                    putExtra(Constants.EXTRA_USER, mUserViewModel.mUser.value);
                    startActivity(this);
                }
            }

        })

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(mSigninEventReceiver, IntentFilter(Constants.ACTION_SIGNIN))

    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mSigninEventReceiver);
        super.onDestroy()

    }
    private fun refresh(){
        mUserViewModel.mUser.value?.mEmail?.let {
            //내 토픽을 가져온다
            mTopicViewModel.getTopics(it)
        };

    }

}