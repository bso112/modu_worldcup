package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureCommentDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.helper.Constants.EXTRA_TOPICMODEL
import com.manta.worldcup.helper.Constants.EXTRA_USER
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Math.pow
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.log2

class GameActivity : AppCompatActivity() {

    private lateinit var mPictureModels: ArrayList<PictureModel>;
    private lateinit var mTopic: Topic;
    private lateinit var mPlayer: User;
    //토픽에 있는 사진의 총 갯수
    private var mPictureSum = 0;

    private val mMasterViewModel: MasterViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MasterViewModel(application) as T;
            }

        }).get(MasterViewModel::class.java);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mTopic = intent.getSerializableExtra(EXTRA_TOPICMODEL) as? Topic ?: return;
        mPlayer = intent.getSerializableExtra(EXTRA_USER) as? User ?: return;

        //토픽이 포함하는 사진들을 가져온다.
        mMasterViewModel.getPictures(mTopic.mId);
        //서버에서 받아온 사진 데이터를 copy해서 어댑터에 넣는다.
        mMasterViewModel.mPictures.observe(this, androidx.lifecycle.Observer {
            mPictureModels = ArrayList(it);
            mPictureSum = mPictureModels.size;
            mPictureModels.shuffle();
            showImage();
        })

        iv_A.setOnClickListener { choose(true); showImage(); }
        iv_B.setOnClickListener { choose(false); showImage(); }

        btn_comment_A.setOnClickListener {
            if (mPictureModels.isNotEmpty())
                PictureCommentDialog().newInstance(mPictureModels[0], mPlayer).show(supportFragmentManager, null);
        }
        btn_comment_B.setOnClickListener {
            if (mPictureModels.size >= 2)
                PictureCommentDialog().newInstance(mPictureModels[1], mPlayer).show(supportFragmentManager, null);
        }


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
        if (isA) mPictureModels.removeAt(1); else mPictureModels.removeAt(0);

        //결과를 셔플
        mPictureModels.shuffle();

        //사진이 하나만 남으면 우승결정됨
        if (mPictureModels.size < 2) {
            //플레이어가 토픽 주최자면 포인트는 얻지 못한다.
            if (mPlayer.mEmail != mPictureModels.first().mOwnerEmail) {
                //토픽 주최자에게 포인트 플러스
                mMasterViewModel.addPoint(Constants.POINT_END_GAME, mTopic.mManagerEmail);
                //이긴 사진의 주인에게 포인트 플러스
                mMasterViewModel.addPoint(Constants.POINT_WIN_PICTURE, mPictureModels.first().mOwnerEmail);
            }
            //사진의 winCnt 증가
            mMasterViewModel.addWinCnt(mPictureModels.first().mId);

            Intent(this, GameResultActivity::class.java).apply {
                putExtra(Constants.EXTRA_TOPICMODEL, mTopic);
                putExtra(Constants.EXTRA_PICTUREMODEL, mPictureModels.first());
                putExtra(Constants.EXTRA_USER, mPlayer);
                startActivity(this);
                finish();
                return;
            }
        }
    }

    fun showImage() {
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