package com.manta.worldcup.activity.fragment.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.CommentAdapter
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.CommentViewModel
import com.manta.worldcup.viewmodel.UserViewModel
import com.skydoves.balloon.ArrowConstraints
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.dialog_topic_comment.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 토픽에 코멘트를 달 수 있게 띄우는 다이어로그다.
 */
class TopicCommentDialog : DialogFragment() {

    private val mCommentViewModel: CommentViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(requireActivity().application) as T; }
        }).get(CommentViewModel::class.java);
    }

    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T; }
        }).get(UserViewModel::class.java);
    }

    private lateinit var mCommentAdapter: CommentAdapter

    /**
     * 현재 유저가 지정한 부모댓글
     */
    //private var mParentComment : Comment? = null;

    private var mCommentReplyTo: Long? = null

    /**
     * @param topic : 댓글을 쓸 토픽의 정보
     * @param user : 댓글을 쓰는 유저의 정보
     */
    fun newInstance(topic: Topic, user: User): TopicCommentDialog {
        val args = Bundle(2);
        args.putSerializable(Constants.EXTRA_TOPIC, topic);
        args.putSerializable(Constants.EXTRA_USER, user);
        val fragment = TopicCommentDialog();
        fragment.arguments = args;
        return fragment;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_topic_comment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //인풋모드 설정 (EditText가 키보드에 가려지지않게)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val topic = requireArguments().getSerializable(Constants.EXTRA_TOPIC) as? Topic ?: return;
        val user = requireArguments().getSerializable(Constants.EXTRA_USER) as? User ?: return;

        mCommentAdapter = CommentAdapter(user)
        rv_comment.adapter = mCommentAdapter;
        rv_comment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        mCommentViewModel.mComments.observe(this, androidx.lifecycle.Observer {
            mCommentAdapter.setComments(it);
        })

        mCommentViewModel.getTopicComments(topic.mId);


        cv_reply.setOnClickListener {
            hideReplyCard()
        }

        //코멘트 대댓글
        mCommentAdapter.setOnItemClickListener(object : CommentAdapter.OnItemClickListener {
            override fun onItemClick(comment: Comment) {
                mCommentReplyTo = comment.mId;
                cv_reply.visibility = View.VISIBLE
                tv_reply_to.text = comment.mWriter;
            }
        })

        //내 코멘트 수정
        mCommentAdapter.setOnCommentChangeListener(object : CommentAdapter.OnCommentChangeListener {
            override fun onCommentDelete(comment: Comment) {
                mCommentViewModel.deleteTopicComment(comment)
            }

            override fun onMoreButtonClick() {
                hideReplyCard()
            }

            override fun onCommentUpdate(comment: Comment) {
                mCommentViewModel.updateTopicComment(comment);
            }
        })

        //코멘트 좋아요 싫어요
        mCommentAdapter.setOnRecommendBtnClickListener(object : CommentAdapter.OnRecommendBtnClickListener {
            override fun onRecommendChanged(commentID: Long, good: Int, bad: Int) {
                mCommentViewModel.updateTopicCommentRecommend(commentID, good, bad)
            }


        })

        //댓글 제출
        btn_send.setOnClickListener {
            val date = Calendar.getInstance().time;
            val locale = requireContext().applicationContext.resources.configuration.locale;

            //루트 댓글을 찾는다.
            if (mCommentReplyTo != null) {
                mCommentReplyTo = mCommentAdapter.getRootCommentID(mCommentReplyTo!!);
            }

            val comment = Comment(
                0,
                user.mNickname,
                user.mEmail,
                et_comment.text.toString(),
                SimpleDateFormat("yyyy.MM.dd HH:mm", locale).format(date),
                topic.mId,
                topic.mManagerEmail,
                mParentID = mCommentReplyTo
            )

            mCommentViewModel.insertTopicComment(comment);
            //작성 후 덧글창 비우기
            et_comment.setText("");
            //리플라이 대상 초기화
            hideReplyCard()

            //키보드 내리기
            val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(et_comment.windowToken, 0)
        }


        tv_title.text = topic.mTitle
        tv_managerName.text = topic.mManagerName


        val url = Constants.BASE_URL + "image/get/${topic.mId}/0"
        Constants.GlideWithHeader(url, iv_picture, iv_picture, requireContext(), true)
        //토픽 주인의 티어가 어딘지 가져오기
        mUserViewModel.getUser(topic.mManagerEmail);
        mUserViewModel.mUser.observe(this, androidx.lifecycle.Observer {
            it?.let{iv_tier.setImageResource(it.mTier)}
        })



        tv_income.text = (topic.mView * Constants.POINT_CLEAR_GAME).toString();


        btn_info.setOnClickListener {
            val balloon: Balloon = Balloon.Builder(requireContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                .setPadding(6)
                .setArrowPosition(0.5f)
                .setCornerRadius(10f)
                .setBackgroundColorResource(R.color.yellow)
                .setTextColorResource(R.color.black)
                .setText(resources.getString(R.string.tooltip_topic_income) + " " + Constants.POINT_CLEAR_GAME)
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build()

            balloon.show(btn_info)
        }


        tv_like.text = topic.mLike.toString()
        tv_dislike.text = topic.mDislike.toString()
    }

    private fun hideReplyCard() {
        //리플라이 대상 초기화
        mCommentReplyTo = null;
        cv_reply.visibility = View.GONE;
    }


}