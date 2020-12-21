package com.manta.worldcup.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.TopicPictureAdapter
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.activity_add_topic.*
import java.text.SimpleDateFormat
import java.util.*

class AddTopicActivity : AppCompatActivity() {

    private val mMasterViewModel: MasterViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MasterViewModel(application) as T;
            }

        }).get(MasterViewModel::class.java);
    }
    private lateinit var mTopicPictureAdapter: TopicPictureAdapter;

    val REQUEST_PICK_FROM_ALBUM = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        //잘못된 접근(유저이메일이 없는 상태로 접근)을 제한한다.
        val user = intent.getSerializableExtra(Constants.EXTRA_USER) as? User ?: return;


        mTopicPictureAdapter = TopicPictureAdapter(supportFragmentManager);

        btn_add_Picture.setOnClickListener {
            pickPictureFromGallay();
        }

        rv_picture.adapter = mTopicPictureAdapter;
        rv_picture.layoutManager = GridLayoutManager(this, 3)

        btn_submit.setOnClickListener {
            if (!mTopicPictureAdapter.isPicturesReadyToSubmit()) {
                Toast.makeText(this, resources.getString(R.string.warn_unnamed_picture), Toast.LENGTH_SHORT).show();
            }else if(mTopicPictureAdapter.getPictureSize() < 2)
                Toast.makeText(this, resources.getString(R.string.warn_not_enough_picture), Toast.LENGTH_SHORT).show();
            else if(user.mCurrPoint < kotlin.math.abs(Constants.POINT_ADD_TOPIC))
                Toast.makeText(this, resources.getString(R.string.warn_not_enough_point), Toast.LENGTH_SHORT).show();
            else{
                //제출
                //포인트소모
                mMasterViewModel.addPoint(Constants.POINT_ADD_TOPIC, user.mEmail);
                //토픽생성
                val date = Calendar.getInstance().time;
                val locale = applicationContext.resources.configuration.locale;
                mMasterViewModel.insertTopic(
                    Topic(0, SimpleDateFormat("yyyy.MM.dd HH:mm", locale).format(date),et_title.text.toString(), et_content.text.toString(), user.mNickname, 0, user.mEmail),
                    mTopicPictureAdapter.getPictures()
                )


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
                mTopicPictureAdapter.addBitmap(bitmap, user.mEmail);

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