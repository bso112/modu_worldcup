package com.manta.worldcup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.manta.worldcup.R
import com.manta.worldcup.adapter.ImageAdaper

class AddPictureActivity : AppCompatActivity() {
    val mImageAdapter  = ImageAdaper();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_picture)


    }


}