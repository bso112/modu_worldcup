package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.TopicPictureAdapter
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Picture
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.PictureViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_update_topic.btn_add_Picture
import kotlinx.android.synthetic.main.activity_update_topic.btn_submit
import kotlinx.android.synthetic.main.activity_update_topic.et_content
import kotlinx.android.synthetic.main.activity_update_topic.et_title
import kotlinx.android.synthetic.main.activity_update_topic.rv_picture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 월드컵을 수정하기 위한 액티비티.
 */
class UpdateTopicActivity : AppCompatActivity() {

    private val mPictureViewModel: PictureViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PictureViewModel(application) as T;
            }

        }).get(PictureViewModel::class.java);
    }

    private val mTopicViewModel : TopicViewModel by lazy{
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(application) as T;
            }

        }).get(TopicViewModel::class.java);
    }

    private val mTopicPictureAdapter: TopicPictureAdapter by lazy {
        TopicPictureAdapter(supportFragmentManager);
    }

    private val REQUEST_PICK_FROM_ALBUM = 0;

    private var originalPictureModels = emptyList<PictureModel>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_topic)

        val topic = intent.getSerializableExtra(Constants.EXTRA_TOPIC) as? Topic ?: return;

        et_title.setText(topic.mTitle);
        et_content.setText(topic.mDescription);

        mPictureViewModel.getPictures(topic.mId);

        rv_picture.adapter = mTopicPictureAdapter;
        rv_picture.layoutManager = GridLayoutManager(this, 3)

        mPictureViewModel.mPictures.observe(this, Observer {
            originalPictureModels = it;
            mTopicPictureAdapter.setPictures(it)
        })


        btn_add_Picture.setOnClickListener {
            pickPictureFromGallay();
        }

        btn_submit.setOnClickListener {
            if (!mTopicPictureAdapter.isPicturesReadyToSubmit()) {
                Toast.makeText(this, resources.getString(R.string.warn_unnamed_picture), Toast.LENGTH_SHORT).show();
            }else if(mTopicPictureAdapter.getPictureSize() < 2)
                Toast.makeText(this, resources.getString(R.string.warn_not_enough_picture), Toast.LENGTH_SHORT).show();
            else{
                //제출
                //토픽업데이트
                topic.mDescription = et_content.text.toString();
                topic.mTitle = et_title.text.toString();
                topic.mImageLength = mTopicPictureAdapter.getPictureSize();
                mTopicViewModel.updateTopic(topic)


                val currentPictures = mTopicPictureAdapter.getPictures()
                val picturesToDelete = mutableListOf<PictureModel>()
                val picturesToInsert = mutableListOf<Picture>()
                //토픽에 원래 있던 사진과 현재 있는 사진을 비교해서 삭제해야할 사진을 골라낸다.
                for(origin in originalPictureModels){
                    var isContain = false;
                    for(current in currentPictures){
                        if(current.pictureModel.mId == origin.mId)
                            isContain = true;
                    }

                    if(!isContain)
                        picturesToDelete.add(origin);
                }
                //새로 추가해야할 사진을 골라낸다.
                for(current in currentPictures){
                    //아이디가 초기값인 것은 새로추가된 사진임.
                    if(current.pictureModel.mId == 0L)
                        picturesToInsert.add(current)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    //삭제된 사진 삭제
                    mPictureViewModel.deletePictures(picturesToDelete);
                }

                //새로운 사진 추가
                mPictureViewModel.insertPictureToTopic(picturesToInsert, topic.mId);

                setResult(RESULT_OK);
                finish();

            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val user = intent.getSerializableExtra(Constants.EXTRA_USER) as? User ?: return;
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_FROM_ALBUM) {
            val bitmapList = BitmapHelper.getBitmapFromIntent(data, contentResolver, Intent.ACTION_GET_CONTENT);
            for (bitmap in bitmapList)
                mTopicPictureAdapter.addPicture(bitmap, user.mEmail);

        }
    }

    private fun pickPictureFromGallay() {
        Intent(Intent.ACTION_GET_CONTENT).let {
            it.type = "image/*"
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(it, "Select Picture"), REQUEST_PICK_FROM_ALBUM)
        }
    }


}