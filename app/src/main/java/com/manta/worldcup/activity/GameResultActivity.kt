package com.manta.worldcup.activity

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_game_result.*
import kotlin.math.round

/**
 * 웝드컵 게임의 플레이 결과를 보여주는 액티비티
 */
class GameResultActivity : AppCompatActivity() {

    private val mTopicViewModel: TopicViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(application) as T;
            }
        }).get(TopicViewModel::class.java);
    }
//    private val mCommentViewModel : CommentViewModel by lazy{
//        ViewModelProvider(this, object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return CommentViewModel(application) as T;
//            }
//        }).get(CommentViewModel::class.java);
//    }
//    private lateinit var mCommentAdapter : CommentAdapter

    /**
     * 토픽에 좋아요를 눌렀는가?
     * null     : 아무것도 선택하지 않은 상태
     * true     : 종아요를 누른상태
     * false    : 싫어요를 누른상태
     */
    private var mIsLike: Boolean? = null;
    private lateinit var mTopic: Topic;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)


        //인풋모드 설정 (EditText가 키보드에 가려지지않게)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mTopic = (intent.getSerializableExtra(Constants.EXTRA_TOPIC) as? Topic) ?: return;
        val winner = (intent.getSerializableExtra(Constants.EXTRA_PICTURE_MODEL) as? PictureModel) ?: return;
        val player = (intent.getSerializableExtra(Constants.EXTRA_USER) as? User) ?: return;

//        mCommentAdapter = CommentAdapter(player)
//        rv_comment.adapter = mCommentAdapter
//        rv_comment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        mCommentViewModel.getTopicComments(mTopic.mId)
//        mCommentViewModel.mComments.observe(this, Observer {
//            mCommentAdapter.setComments(it)
//        })
//


        tv_winner_name.text = winner.mPictureName;

        //우승 사진 표시
        val url = Constants.BASE_URL + "image/get/${winner.mId}/";
        Constants.GlideWithHeader(url, this, iv_winner, this);


        //다른 결과보기
        tv_show_picture.setOnClickListener {
            Intent(this, StatisticActivity::class.java).apply {
                putExtra(Constants.EXTRA_TOPIC, mTopic);
                putExtra(Constants.EXTRA_USER, player);
                startActivity(this);

            }
        }


        //좋아요 싫어요
        tv_like.text = mTopic.mLike.toString();
        tv_dislike.text = mTopic.mDislike.toString();

        val oldLike = mTopic.mLike;
        val oldDislike = mTopic.mDislike;

        var typedValue: TypedValue = TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        val primaryColor = typedValue.data;

        btn_like.setOnClickListener {
            if (tv_like.text != oldLike.toString()) {
                uncheckLikeButton(oldLike);
                mIsLike = null
            } else {
                //check like button
                btn_like.backgroundTintList = ColorStateList.valueOf(primaryColor);
                tv_like.text = (oldLike + 1).toString();
                mIsLike = true;
            }
            unCheckDislikeButton(oldDislike)
        }

        btn_dislike.setOnClickListener {
            if (tv_dislike.text != oldDislike.toString()) {
                unCheckDislikeButton(oldDislike)
                mIsLike = null;
            } else {
                //check dislike button
                btn_dislike.backgroundTintList = ColorStateList.valueOf(primaryColor);
                tv_dislike.text = (oldDislike + 1).toString();
                mIsLike = false;
            }

            uncheckLikeButton(oldLike);

        }

        //0으로 나누기 방지
        val percent = if(mTopic.mView == 0) 0 else (winner.mWinCnt.toFloat() / mTopic.mView.toFloat() * 100).toInt()
        tv_percent.text = percent.toString() + "%";

    }

    private fun uncheckLikeButton(oldLike: Int) {
        btn_like.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.disabled));
        tv_like.text = oldLike.toString();
    }

    private fun unCheckDislikeButton(oldDislike: Int) {
        btn_dislike.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.disabled));
        tv_dislike.text = oldDislike.toString();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mIsLike?.let {
            mTopicViewModel.UpdateRecommend(it, mTopic.mId)
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                Intent().apply { action = Constants.ACTION_NEED_REFRESH })
        }

    }


}