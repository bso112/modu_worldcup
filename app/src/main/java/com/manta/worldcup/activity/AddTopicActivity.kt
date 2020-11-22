package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.manta.worldcup.R
import com.manta.worldcup.adapter.ImageAdaper
import com.manta.worldcup.helper.BitmapHelper
import kotlinx.android.synthetic.main.activity_add_topic.*

class AddTopicActivity : AppCompatActivity() {

    val mImageAdapter  = ImageAdaper();
    val REQUEST_PICK_FROM_ALBUM = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        btn_addPicture.setOnClickListener {
            pickPictureFromGallay();
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == REQUEST_PICK_FROM_ALBUM){
              val bitmapList = BitmapHelper.getBitmapFromIntent(data, contentResolver, Intent.ACTION_GET_CONTENT);
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