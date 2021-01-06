package com.manta.worldcup.activity.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.OnTopicClickDialog
import com.manta.worldcup.adapter.RecentTopicAdapter
import com.manta.worldcup.adapter.TopicAdapter_Top10
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.viewmodel.UserViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.frag_home.*
import kotlin.math.min


/**
 * 홈화면
 */
class HomeFragment : Fragment(R.layout.frag_home) {
    private val mTopicViewModel: TopicViewModel by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TopicViewModel(requireActivity().application) as T;
        }}).get(TopicViewModel::class.java);
    };
    private val mUserViewModel: UserViewModel by lazy{
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);
    };

    private val mRecentTopicAdpater  = RecentTopicAdapter().apply {
        setOnTopicClickListener(object : RecentTopicAdapter.OnTopicClickListener{
            override fun onTopicClick(topicJoinUser : TopicJoinUser) {
                if (mUserViewModel.mUser.value == null) {
                    Toast.makeText(context, resources.getString(R.string.need_signin), Toast.LENGTH_SHORT).show()
                    return@onTopicClick;
                }
                fragmentManager?.let { OnTopicClickDialog().newInstance(topicJoinUser.getTopic(), mUserViewModel.mUser.value!!).show(it, null) };
            }
        })

    }
    //탑 10 추천순위 월드컵을 보여주는 어댑터
    private lateinit var mTopicAdaptorTop10Recommend: TopicAdapter_Top10;
    //탑 10 조회수순위 월드컵을 보여주는 어댑터
    private lateinit var mTopicAdaptorTop10View: TopicAdapter_Top10;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity == null) return;
        if (context == null) return;

        mTopicAdaptorTop10Recommend = TopicAdapter_Top10();
        mTopicAdaptorTop10View = TopicAdapter_Top10();

        //최근 10개 월드컵을 보여주는 ViewPager 설정. 인디케이터도 같이 설정한다.
        vp_recent_topic.adapter = mRecentTopicAdpater;
        vp_recent_topic.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page_indicator.selectDot(position)
            }
        })

        rv_top10_recommend.adapter = mTopicAdaptorTop10Recommend;
        rv_top10_recommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        rv_top10_view.adapter = mTopicAdaptorTop10View;
        rv_top10_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)




        mTopicViewModel.mDataset.observe(this, Observer { topics ->
            val copyList = topics.toMutableList(); //sort하려면 mutableList여야하는듯
            //subList는 새로운 List를 만드는게 아닌 것 같다. toMutableList로 새로운 리스트를 만들어야함.
            //최근 5개 토픽을 보여준다.
            mRecentTopicAdpater.setTopicJoinUsers(topics.subList(0, min(5, copyList.size)).toMutableList())
            //인디케이터 생성
            page_indicator.createDotPanel(min(5, copyList.size), R.drawable.circle_indicator_dot, R.drawable.circle_indicator_dot_selected, 0)

            copyList.sortByDescending { it.mLike }
            mTopicAdaptorTop10Recommend.setTopics(copyList.subList(0, min(10, copyList.size)).toMutableList())
            copyList.sortByDescending { it.mView }
            mTopicAdaptorTop10View.setTopics(copyList.subList(0, min(10, copyList.size)).toMutableList())


        })



        //월드컵 클릭시
        val onTopicClick = onTopicClick@ fun(topicJoinUser: TopicJoinUser) {
            if (mUserViewModel.mUser.value == null) {
                Toast.makeText(context, resources.getString(R.string.need_signin), Toast.LENGTH_SHORT).show()
                return@onTopicClick;
            }
            fragmentManager?.let { OnTopicClickDialog().newInstance(topicJoinUser.getTopic(), mUserViewModel.mUser.value!!).show(it, null) };
        }

        mTopicAdaptorTop10Recommend.setOnItemClickListener(
            object : TopicAdapter_Top10.OnItemClickListener {
                override fun onItemClick(topicJoinUser: TopicJoinUser) {
                    onTopicClick(topicJoinUser)
                }
            })


        mTopicAdaptorTop10View.setOnItemClickListener(
            object : TopicAdapter_Top10.OnItemClickListener {
                override fun onItemClick(topicJoinUser: TopicJoinUser) {
                    onTopicClick(topicJoinUser)
                }
            })

    }


}