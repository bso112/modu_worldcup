package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.helper.Constants.EXTRA_TOPICMODEL
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity() {

    private lateinit var mPictureModels: ArrayList<PictureModel>;
    private lateinit var mTopicModel: TopicModel;

    private var mGameReady = false;
    private val mViewModel: TopicViewModel by lazy {
        ViewModelProvider(this).get(TopicViewModel::class.java);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mTopicModel = intent.getSerializableExtra(EXTRA_TOPICMODEL) as? TopicModel ?: return;

        //토픽이 포함하는 사진들을 가져온다.
        mViewModel.getPictures(mTopicModel.mId);
        //서버에서 받아온 데이터를 copy
        mViewModel.mPictures.observe(this, androidx.lifecycle.Observer {
            mPictureModels = ArrayList(it);
            mPictureModels.shuffle();
            showImage();
            mGameReady = true;
        })

        iv_A.setOnClickListener { choose(true); showImage(); }
        iv_B.setOnClickListener {
            choose(false); showImage();
        }


    }


    /**
     * by 변성욱
     * 두 사진 중 하나를 고른다. 위쪽 사진을 골랐으면 true를 인자로 넣는다.
     */
    fun choose(isA: Boolean) {
        if (mPictureModels.size < 2) {
            if (mGameReady && mPictureModels.isNotEmpty())
                Intent(this, GameResultActivity::class.java).apply {
                    putExtra(Constants.EXTRA_TOPICMODEL, mTopicModel);
                    putExtra(Constants.EXTRA_PICTUREMODEL, mPictureModels.first());
                    startActivity(this);
                    finish();
                }
            return;
        }

        if (isA) mPictureModels.removeAt(0); else mPictureModels.removeAt(1);
    }

    fun showImage() {
        if (mPictureModels.size < 2) return;
        Glide.with(this).load(Constants.BASE_URL + "image/get/${mPictureModels[0].mId}/").into(iv_A);
        Glide.with(this).load(Constants.BASE_URL + "image/get/${mPictureModels[1].mId}/").into(iv_B);
    }


}