package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureDescriptionDialog
import com.manta.worldcup.adapter.PictureAdapter
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_add_picture.btn_add_Picture
import kotlinx.android.synthetic.main.activity_add_picture.rv_picture
import kotlinx.android.synthetic.main.activity_add_topic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPictureActivity : AppCompatActivity() {
    lateinit var mPictureAdapter : PictureAdapter;
    val REQUEST_PICK_FROM_ALBUM = 0;
    var mTopicImageNames: HashSet<String>? = null;

    private val mViewModel: TopicViewModel by lazy {
        ViewModelProvider(this).get(TopicViewModel::class.java);
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_picture)

        mPictureAdapter = PictureAdapter(supportFragmentManager);

        val topicId = intent.getLongExtra(Constants.EXTRA_TOPIC_ID, -1);
        if (topicId < 0) {
            Toast.makeText(this, "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

        //현재토픽에 등록된 사진들의 이름목록 가져오기
        CoroutineScope(Dispatchers.IO).launch {
            val result = mViewModel.getTopicImageNames(topicId)
            if (result.isSuccessful) {
                mTopicImageNames = result.body()
                mPictureAdapter.setPictureNames(mTopicImageNames!!);
            }
        }



        rv_picture.adapter = mPictureAdapter;
        rv_picture.layoutManager = LinearLayoutManager(this, GridLayoutManager.VERTICAL, false)

        //갤러리에서 사진가져오기
        btn_add_Picture.setOnClickListener {
            pickPictureFromGallay();
        }

        //중복된 사진이 있으면 제출불가
        btn_submit.setOnClickListener {
            mViewModel.insertPictureToTopic(mPictureAdapter.getPictures(), topicId);
            finish();
        }
    }

    private fun isNameUnique(pictureName: String): Boolean {
        mTopicImageNames ?: return true;
        return !mTopicImageNames!!.contains(pictureName);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_FROM_ALBUM) {
            val bitmapList = BitmapHelper.getBitmapFromIntent(data, contentResolver, Intent.ACTION_GET_CONTENT);
            for (bitmap in bitmapList)
                mPictureAdapter.addBitmap(bitmap);

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