package com.manta.worldcup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.TopicCommentDialog
import com.manta.worldcup.adapter.CommentAdapter
import com.manta.worldcup.adapter.MyPictureAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.CommentViewModel
import com.manta.worldcup.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.activity_statistic.*
import kotlinx.android.synthetic.main.activity_statistic.rv_picture

class StatisticActivity : AppCompatActivity() {

    private val mMyPictureAdapter  = MyPictureAdapter();
   // private val mCommentAdapter = CommentAdapter();
    private val mMasterViewModel: MasterViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MasterViewModel(application) as T;
            }

        }).get(MasterViewModel::class.java);
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

        val topic = intent.getSerializableExtra(Constants.EXTRA_TOPICMODEL) as? Topic ?: return;
        val user = intent.getSerializableExtra(Constants.EXTRA_USER) as? User ?: return;

        rv_picture.adapter = mMyPictureAdapter
        rv_picture.layoutManager = GridLayoutManager(this, 2 );

//        rv_comment.adapter = mCommentAdapter;
//        rv_comment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mMasterViewModel.mPictures.observe(this, Observer { mMyPictureAdapter.setPictures(ArrayList(it)) })
       // mCommentViewModel.mComments.observe(this, Observer { mCommentAdapter.setComments(it); })

        mMasterViewModel.getPictures(topic.mId)
        //mCommentViewModel.getTopicComments(topicId);

        tv_show_comment.setOnClickListener {
            TopicCommentDialog().newInstance(topic, user).show(supportFragmentManager, null);
        }
    }
}