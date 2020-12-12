package com.manta.worldcup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.TopicCommentDialog
import com.manta.worldcup.adapter.CommentAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.CommentViewModel
import kotlinx.android.synthetic.main.activity_game_result.*

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

        val topicModel = (intent.getSerializableExtra(Constants.EXTRA_TOPICMODEL) as? Topic)  ?: return;
        val winner = (intent.getSerializableExtra(Constants.EXTRA_PICTUREMODEL) as? PictureModel) ?: return;
        val player = (intent.getSerializableExtra(Constants.EXTRA_USER) as? User) ?: return;

        tv_winner_name.text = winner.mPictureName;

        //우승 사진 표시
        val url = Constants.BASE_URL + "image/get/${winner.mId}/";
        Constants.GlideWithHeader(url, this, iv_winner, this);



        mCommentViewModel.mComments.observe(this, Observer {
            mCommentAdapter.setComments(it);
        })

        mCommentViewModel.getTopicComments(topicModel.mId);

        //다른 결과보기
        tv_show_picture.setOnClickListener {
            Intent(this, StatisticActivity::class.java).apply {
                putExtra(Constants.EXTRA_TOPICMODEL, topicModel);
                putExtra(Constants.EXTRA_USER, player);
                startActivity(this);
                
            }
        }
    }


}