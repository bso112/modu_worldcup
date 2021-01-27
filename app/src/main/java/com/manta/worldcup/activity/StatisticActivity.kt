package com.manta.worldcup.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.adapter.PictureAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.PictureViewModel
import com.skydoves.balloon.*
import kotlinx.android.synthetic.main.activity_statistic.*
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.TopicCommentDialog
import com.manta.worldcup.adapter.StatisticPictureAdpater
import com.manta.worldcup.viewmodel.TopicViewModel

/**
 * 월드컵의 결과를 통계적으로 보여주는 액티비티
 */
class StatisticActivity : AppCompatActivity() {

    private lateinit var mPictureAdapter : StatisticPictureAdpater;
    private val mPictureViewModel: PictureViewModel by lazy {
        PictureViewModel.provideViewModel(this, application);
    }
    private val mTopicViewModel : TopicViewModel by lazy{
        TopicViewModel.provideViewModel(this, application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        val topic = intent.getSerializableExtra(Constants.EXTRA_TOPIC) as? Topic ?: return;
        val user = intent.getSerializableExtra(Constants.EXTRA_USER) as? User ?: return;

        mPictureAdapter = StatisticPictureAdpater(supportFragmentManager);
        mPictureAdapter.setUser(user);
        rv_picture.adapter = mPictureAdapter
        rv_picture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_picture.isNestedScrollingEnabled = false;

        mPictureViewModel.mPictures.observe(this, Observer {
            mPictureAdapter.setPictures(ArrayList(it), true) }
        )

        mPictureViewModel.getPictures(topic.mId)

        tv_show_comment.setOnClickListener {
            TopicCommentDialog().newInstance(topic, user).show(supportFragmentManager, null);
        }

    }


}