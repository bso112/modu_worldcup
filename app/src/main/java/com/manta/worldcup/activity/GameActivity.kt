package com.manta.worldcup.activity

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureInfoDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.helper.Constants.EXTRA_TOPIC
import com.manta.worldcup.helper.Constants.EXTRA_USER
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.PictureViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Math.pow
import kotlin.math.ceil
import kotlin.math.log2

/**
 * 실제로 월드컵을 진행하는 액티비티
 */
class GameActivity : AppCompatActivity() {

    private lateinit var mPictureModels: ArrayList<PictureModel>;
    private lateinit var mTopic: Topic;
    private lateinit var mPlayer: User;

    //토픽에 있는 사진의 총 갯수
    private var mPictureSum = 0;


    private val mPictureViewModel: PictureViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PictureViewModel(application) as T;
            }

        }).get(PictureViewModel::class.java);
    }

    private val mTopicViewModel: TopicViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(application) as T;
            }
        }).get(TopicViewModel::class.java);
    }


    private val mOutUpwardAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_out_upward_screen)
    }

    private val mOutDownwardAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_out_downward_screen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mTopic = intent.getSerializableExtra(EXTRA_TOPIC) as? Topic ?: return;
        mPlayer = intent.getSerializableExtra(EXTRA_USER) as? User ?: return;

        //토픽이 포함하는 사진들을 가져온다.
        mPictureViewModel.getPictures(mTopic.mId);
        //서버에서 받아온 사진 데이터를 copy해서 어댑터에 넣는다.
        mPictureViewModel.mPictures.observe(this, androidx.lifecycle.Observer {
            mPictureModels = ArrayList(it);
            mPictureSum = mPictureModels.size;
            mPictureModels.shuffle();
            showImage();
        })

        iv_A.setOnClickListener { choose(true);  }
        iv_B.setOnClickListener { choose(false);  }

        btn_comment_A.setOnClickListener {
            if (mPictureModels.isNotEmpty())
                PictureInfoDialog().newInstance(mPictureModels[0], mPlayer).show(supportFragmentManager, null);
        }
        btn_comment_B.setOnClickListener {
            if (mPictureModels.size >= 2)
                PictureInfoDialog().newInstance(mPictureModels[1], mPlayer).show(supportFragmentManager, null);
        }
    }


    private fun moveViewToScreenCenter(view: View) {
        val root = findViewById<View>(R.id.root) as ConstraintLayout
        val dm = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(dm)
        val statusBarOffset = dm.heightPixels - root.measuredHeight
        val originalPos = IntArray(2)
        view.getLocationOnScreen(originalPos)
        val yDest = dm.heightPixels / 2 - view.measuredHeight / 2 + statusBarOffset
        val anim = TranslateAnimation(0F, 0F, 0F, (yDest - originalPos[1]).toFloat())
        anim.duration = (resources.getInteger(R.integer.game_anim_duration) / 1.5).toLong()
        //애니메이션이 끝난 후에도 자리를 유지한다.
        anim.fillAfter = true;
        view.startAnimation(anim)
        //모든 다른 애니메이션이 끝나면 제자리로 돌아간다.
        android.os.Handler(Looper.getMainLooper()).postDelayed(Runnable {
            anim.cancel()
            onAnimationEnd()
        }, resources.getInteger(R.integer.game_anim_duration).toLong())
    }

    /**
     * by 변성욱
     * 두 사진 중 하나를 고른다. 위쪽 사진을 골랐으면 true를 인자로 넣는다.
     */
    fun choose(isA: Boolean) {

        //예외처리
        if (mPictureModels.size < 2)
            return;

        //고르지 않은것을 지운다.
        if (isA) {
            mPictureModels.removeAt(1)
            moveViewToScreenCenter(cv_A)
            cv_B.startAnimation(mOutDownwardAnim)
            tv_vs.startAnimation(mOutDownwardAnim)
        } else {
            mPictureModels.removeAt(0)
            moveViewToScreenCenter(cv_B)
            cv_A.startAnimation(mOutUpwardAnim)
            tv_vs.startAnimation(mOutUpwardAnim)
        }


    }

    private fun onAnimationEnd() {
        //결과를 셔플
        mPictureModels.shuffle();

        //사진이 하나만 남으면 우승결정됨
        if (mPictureModels.size < 2) {
            //내가 내 사진을 우승시키면 포인트를 얻지 못한다.
            //플레이어가 토픽 주최자면 포인트는 얻지 못한다. (스스로만든 월드컵을 플레이해서는 포인트 획득못함)
            if (mPlayer.mEmail != mPictureModels.first().mOwnerEmail && mPlayer.mEmail != mTopic.mManagerEmail) {
                //토픽 주최자에게 포인트 플러스
                mPictureViewModel.addPoint(Constants.POINT_CLEAR_GAME, mTopic.mManagerEmail);
                //이긴 사진의 주인에게 포인트 플러스
                mPictureViewModel.addPoint(Constants.POINT_WIN_PICTURE, mPictureModels.first().mOwnerEmail);
                //토픽의 view 증가
                mTopicViewModel.increaseView(mTopic.mId);
                //이긴 사진의 winCnt 증가
                mPictureViewModel.addWinCnt(mPictureModels.first().mId);

            }

            Intent(this, GameResultActivity::class.java).apply {
                putExtra(Constants.EXTRA_TOPIC, mTopic);
                putExtra(Constants.EXTRA_PICTURE_MODEL, mPictureModels.first());
                putExtra(Constants.EXTRA_USER, mPlayer);
                startActivity(this);
                finish();
                return;
            }
        }

        showImage();
    }


    private fun showImage() {
        if (mPictureModels.size < 2) return;
        val round = pow(2.0, ceil(log2(mPictureModels.size.toDouble()))).toInt();
        var title = "";
        if (round == 2)
            title = "결승 (${mPictureModels.size}/${mPictureSum})"
        else
            title = "${round}강 (${mPictureModels.size}/${mPictureSum})"

        tv_title.text = title;
        tv_picture_name_A.text = mPictureModels[0].mPictureName;
        tv_picture_name_B.text = mPictureModels[1].mPictureName;
        val url1 = Constants.BASE_URL + "image/get/${mPictureModels[0].mId}/";
        Constants.GlideWithHeader(url1, this, iv_A, this);
        val url2 = Constants.BASE_URL + "image/get/${mPictureModels[1].mId}/";
        Constants.GlideWithHeader(url2, this, iv_B, this);

    }


}