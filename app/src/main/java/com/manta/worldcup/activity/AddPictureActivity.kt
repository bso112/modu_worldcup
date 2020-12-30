package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.TopicPictureAdapter
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.viewmodel.PictureViewModel
import com.skydoves.balloon.ArrowConstraints
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.activity_add_picture.*
import kotlinx.android.synthetic.main.activity_add_picture.btn_add_Picture
import kotlinx.android.synthetic.main.activity_add_picture.rv_picture
import kotlinx.android.synthetic.main.activity_add_topic.*
import kotlinx.android.synthetic.main.activity_add_topic.btn_submit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 사진 추가가 가능한 액티비티
 */
class AddPictureActivity : AppCompatActivity() {
    lateinit var mTopicPictureAdapter: TopicPictureAdapter;
    val REQUEST_PICK_FROM_ALBUM = 0;
    var mTopicImageNames: HashSet<String>? = null;

    private val mViewModel: PictureViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PictureViewModel(application) as T;
            }

        }).get(PictureViewModel::class.java);
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_picture)

        //잘못된 접근(유저이메일이 없는 상태로 접근) 을 제한한다.
        val userEmail = intent.getStringExtra(Constants.EXTRA_USER_EMAIL);
        if(userEmail == null) {
            Log.d(javaClass.toString(), "cannot find user email");
            finish()
        };


        mTopicPictureAdapter = TopicPictureAdapter(supportFragmentManager);

        val topicId = intent.getLongExtra(Constants.EXTRA_TOPIC_ID, -1);
        if (topicId < 0) {
            Toast.makeText(this, resources.getString(R.string.warn_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        //현재토픽에 등록된 사진들의 이름목록 가져오기
        CoroutineScope(Dispatchers.IO).launch {
            val result = mViewModel.getTopicImageNames(topicId)
            if (result.isSuccessful) {
                mTopicImageNames = result.body()
                mTopicPictureAdapter.setPictureNames(mTopicImageNames!!);
            }
        }



        rv_picture.adapter = mTopicPictureAdapter;
        rv_picture.layoutManager = GridLayoutManager(this, 3)

        //갤러리에서 사진가져오기
        btn_add_Picture.setOnClickListener {
            pickPictureFromGallay();
        }

        btn_submit.setOnClickListener {
            if (mTopicPictureAdapter.isPicturesReadyToSubmit()) {
                mViewModel.insertPictureToTopic(mTopicPictureAdapter.getPictures(), topicId);
                finish();
            } else {
                Toast.makeText(this, resources.getString(R.string.warn_unnamed_picture), Toast.LENGTH_SHORT).show();
            }


        }

        btn_help.setOnClickListener {
            val balloon: Balloon = Balloon.Builder(this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                .setPadding(6)
                .setArrowPosition(0.5f)
                .setCornerRadius(10f)
                .setBackgroundColorResource(R.color.yellow)
                .setTextColorResource(R.color.black)
                .setText(resources.getString(R.string.info_add_picture))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build()

            balloon.showAlignBottom(btn_help)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val userEmail = intent.getStringExtra(Constants.EXTRA_USER_EMAIL);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_FROM_ALBUM && userEmail != null) {
            val bitmapList = BitmapHelper.getBitmapFromIntent(data, contentResolver, Intent.ACTION_GET_CONTENT);
            for (bitmap in bitmapList)
                mTopicPictureAdapter.addPicture(bitmap, userEmail);

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