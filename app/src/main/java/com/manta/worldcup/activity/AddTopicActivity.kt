package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.PictureAdapter
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.activity_add_topic.*
import kotlinx.android.synthetic.main.activity_add_topic.et_title

class AddTopicActivity : AppCompatActivity() {

    private val mViewModel: TopicViewModel by lazy {
        ViewModelProvider(this).get(TopicViewModel::class.java);
    }
    private lateinit var mImageAdapter: PictureAdapter;

    val REQUEST_PICK_FROM_ALBUM = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        mImageAdapter = PictureAdapter(supportFragmentManager);

        btn_add_Picture.setOnClickListener {
            pickPictureFromGallay();
        }

        rv_picture.adapter = mImageAdapter;
        rv_picture.layoutManager = GridLayoutManager(this, 3)

        btn_submit.setOnClickListener {
            if (!mImageAdapter.isPicturesReadyToSubmit()) {
                Toast.makeText(this, resources.getString(R.string.warn_unnamed_picture), Toast.LENGTH_SHORT).show();
            }else if(mImageAdapter.getPictureSize() < 2)
                Toast.makeText(this, resources.getString(R.string.warn_not_enough_picture), Toast.LENGTH_SHORT).show();
            else{
                mViewModel.insertTopic(
                    TopicModel(0, et_title.text.toString(), et_content.text.toString(), "관리자", 0),
                    mImageAdapter.getPictures()
                )
                finish();

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_FROM_ALBUM) {
            val bitmapList = BitmapHelper.getBitmapFromIntent(data, contentResolver, Intent.ACTION_GET_CONTENT);
            for (bitmap in bitmapList)
                mImageAdapter.addBitmap(bitmap);

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