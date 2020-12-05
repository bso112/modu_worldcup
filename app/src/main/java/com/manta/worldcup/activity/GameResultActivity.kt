package com.manta.worldcup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.manta.worldcup.R
import com.manta.worldcup.adapter.CommentAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.viewmodel.CommentViewModel
import kotlinx.android.synthetic.main.activity_game_result.*
import kotlinx.android.synthetic.main.activity_game_result.btn_submit
import java.text.SimpleDateFormat
import java.util.*

class GameResultActivity : AppCompatActivity() {

    private val mCommentViewModel: CommentViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(application) as T; }
        }).get(CommentViewModel::class.java);
    }

    private val mCommentAdapter  = CommentAdapter();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        //인풋모드 설정 (EditText가 키보드에 가려지지않게)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val topicModel = intent.getSerializableExtra(Constants.EXTRA_TOPICMODEL) as TopicModel;
        val picturModel = intent.getSerializableExtra(Constants.EXTRA_PICTUREMODEL) as PictureModel;

        tv_winner_name.text = picturModel.mPictureName;

        //우승 사진 표시
        val url = Constants.BASE_URL + "image/get/${picturModel.mId}/";
        Constants.GlideWithHeader(url, this, iv_winner, this);
        //Glide.with(this).load(Constants.BASE_URL + "image/get/${picturModel.mId}/").into(iv_winner);

        //덧글 표시
        rv_comment.adapter = mCommentAdapter;
        rv_comment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mCommentViewModel.mComments.observe(this, Observer {
            mCommentAdapter.setComments(it);
        })

        mCommentViewModel.getTopicComments(topicModel.mId);

        //덧글 작성
        btn_submit.setOnClickListener {
            val date = Calendar.getInstance().time;
            val locale = applicationContext.resources.configuration.locale;

            val comment = Comment(0 ,tv_winner_name.text.toString(), et_content.text.toString(),
                SimpleDateFormat("yyyy.MM.dd HH:mm", locale).format(date), topicModel.mId)
           mCommentViewModel.insertComment(comment);
            //작성 후 덧글창 비우기
            et_content.setText("");
        }

    }


}