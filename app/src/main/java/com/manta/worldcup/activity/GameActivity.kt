package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
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

    private val mViewModel: TopicViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(application) as T;
            }

        }).get(TopicViewModel::class.java);
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
        })

        iv_A.setOnClickListener { choose(true); showImage(); }
        iv_B.setOnClickListener { choose(false); showImage(); }


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
            Intent(this, GameResultActivity::class.java).apply {
                putExtra(Constants.EXTRA_TOPICMODEL, mTopicModel);
                putExtra(Constants.EXTRA_PICTUREMODEL, mPictureModels.first());
                startActivity(this);
                finish();
                return;
            }
        }
    }

    fun showImage() {
        if (mPictureModels.size < 2) return;
        tv_picture_name_A.text = mPictureModels[0].mPictureName;
        tv_picture_name_B.text = mPictureModels[1].mPictureName;
        val url1 = Constants.BASE_URL + "image/get/${mPictureModels[0].mId}/";
        Constants.GlideWithHeader(url1, this, iv_A, this);
        val url2 = Constants.BASE_URL + "image/get/${mPictureModels[1].mId}/";
        Constants.GlideWithHeader(url2, this, iv_B, this);

//        Glide.with(this).load(Constants.BASE_URL + "image/get/${mPictureModels[0].mId}/").into(iv_A);
//        Glide.with(this).load(Constants.BASE_URL + "image/get/${mPictureModels[1].mId}/").into(iv_B);
    }


}