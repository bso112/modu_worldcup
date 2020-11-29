package com.manta.worldcup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants.EXTRA_TOPICMODEL
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.TopicViewModel

class GameActivity : AppCompatActivity() {

    private val mViewModel: TopicViewModel by lazy {
        ViewModelProvider(this).get(TopicViewModel::class.java);
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val topicModel = intent.getSerializableExtra(EXTRA_TOPICMODEL) as? TopicModel ?: return;

        //제목표시

        //이미지받기
        //mViewModel.get
    }
}