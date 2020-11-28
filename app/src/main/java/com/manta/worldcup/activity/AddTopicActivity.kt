package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.ImageAdaper
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.AddTopicViewModel
import kotlinx.android.synthetic.main.activity_add_topic.*
import kotlinx.android.synthetic.main.activity_add_topic.et_title
import kotlinx.android.synthetic.main.item_picture.*

class AddTopicActivity : AppCompatActivity() {

    private val mViewModel : AddTopicViewModel by lazy{
        ViewModelProvider(this).get(AddTopicViewModel::class.java);
    }
    private val mImageAdapter = ImageAdaper();

    val REQUEST_PICK_FROM_ALBUM = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)


        btn_add_Picture.setOnClickListener {
            pickPictureFromGallay();
        }

        rv_picture.adapter = mImageAdapter;
        rv_picture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        btn_submit.setOnClickListener {
            mViewModel.insertTopic(TopicModel(0, et_title.text.toString(), et_description.text.toString() ,"관리자", 0),
            mImageAdapter.getPictures())

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