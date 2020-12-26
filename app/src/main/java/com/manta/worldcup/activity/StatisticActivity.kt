package com.manta.worldcup.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manta.worldcup.adapter.MyPictureAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.CommentViewModel
import com.manta.worldcup.viewmodel.PictureViewModel
import com.skydoves.balloon.*
import kotlinx.android.synthetic.main.activity_statistic.*
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.TopicCommentDialog

/**
 * 월드컵의 결과를 통계적으로 보여주는 액티비티
 */
class StatisticActivity : AppCompatActivity() {

    private lateinit var mMyPictureAdapter : MyPictureAdapter;
   // private val mCommentAdapter = CommentAdapter();
    private val mMasterViewModel: PictureViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PictureViewModel(application) as T;
            }

        }).get(PictureViewModel::class.java);
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

        val topic = intent.getSerializableExtra(Constants.EXTRA_TOPIC) as? Topic ?: return;
        val user = intent.getSerializableExtra(Constants.EXTRA_USER) as? User ?: return;

        mMyPictureAdapter = MyPictureAdapter(supportFragmentManager);
        mMyPictureAdapter.setUser(user);
        rv_picture.adapter = mMyPictureAdapter
        rv_picture.layoutManager = GridLayoutManager(this, 2 );


        mMasterViewModel.mPictures.observe(this, Observer { mMyPictureAdapter.setPictures(ArrayList(it)) })

        mMasterViewModel.getPictures(topic.mId)

        tv_show_comment.setOnClickListener {
            TopicCommentDialog().newInstance(topic, user).show(supportFragmentManager, null);
        }

        tv_earn.text = (topic.mView * Constants.POINT_CLEAR_GAME).toString();




        btn_info.setOnClickListener {
            val balloon: Balloon = Balloon.Builder(this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                .setPadding(6)
                .setArrowPosition(0.5f)
                .setCornerRadius(10f)
                .setBackgroundColorResource(R.color.yellow)
                .setTextColorResource(R.color.black)
                .setText(resources.getString(R.string.tooltip_topic_income) + " " + Constants.POINT_CLEAR_GAME)
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build()

            balloon.show(btn_info)
        }


        tv_Recommend.text = topic.mLike.toString()
        tv_dislike.text = topic.mDislike.toString()
    }


}