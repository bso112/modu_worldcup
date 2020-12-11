package com.manta.worldcup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.CommentAdapter
import com.manta.worldcup.adapter.MyPictureAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.CommentViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_statistic.*
import kotlinx.android.synthetic.main.activity_statistic.rv_picture
import kotlinx.android.synthetic.main.frag_mypicture.*

class StatisticActivity : AppCompatActivity() {

    private val mMyPictureAdapter  = MyPictureAdapter();
    private val mCommentAdapter = CommentAdapter();
    private val mTopicViewModel: TopicViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(application) as T;
            }

        }).get(TopicViewModel::class.java);
    }

    private val mCommentViewModel : CommentViewModel by lazy{
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(application) as T;
            }

        }).get(CommentViewModel::class.java);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        val topicId = intent.getLongExtra(Constants.EXTRA_TOPIC_ID, 0)
        if(topicId == 0.toLong()) {finish(); return;}

        rv_picture.adapter = mMyPictureAdapter
        rv_picture.layoutManager = GridLayoutManager(this, 2 );

        rv_comment.adapter = mCommentAdapter;
        rv_comment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTopicViewModel.mPictures.observe(this, Observer { mMyPictureAdapter.setPictures(ArrayList(it)) })
        mCommentViewModel.mComments.observe(this, Observer { mCommentAdapter.setComments(it); })

        mTopicViewModel.getPictures(topicId)
        mCommentViewModel.getTopicComments(topicId);

    }
}