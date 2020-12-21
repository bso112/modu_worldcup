package com.manta.worldcup.activity.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.worldcup.R
import com.manta.worldcup.adapter.CommentAdapter
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.CommentViewModel
import kotlinx.android.synthetic.main.dialog_comment.*
import java.text.SimpleDateFormat
import java.util.*

class PictureCommentDialog : DialogFragment() {

    private val mCommentViewModel: CommentViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(requireActivity().application) as T; }
        }).get(CommentViewModel::class.java);
    }

    private val mCommentAdapter = CommentAdapter();
    /**
     * 현재 유저가 지정한 부모댓글
     */
    private var mParentComment : Comment? = null;

    fun newInstance(picture: PictureModel, player: User): PictureCommentDialog {
        val args = Bundle(2);
        args.putSerializable(Constants.EXTRA_PICTURE, picture);
        args.putSerializable(Constants.EXTRA_USER, player);
        val fragment = PictureCommentDialog();
        fragment.arguments = args;
        return fragment;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_comment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //인풋모드 설정 (EditText가 키보드에 가려지지않게)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val pictureModel = requireArguments().getSerializable(Constants.EXTRA_PICTURE) as? PictureModel ?: return;
        val player = requireArguments().getSerializable(Constants.EXTRA_USER) as? User ?: return;

        rv_comment.adapter = mCommentAdapter;
        rv_comment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        mCommentAdapter.setOnItemClickListener(object : CommentAdapter.OnItemClickListener{
            override fun OnItemClick(comment : Comment, isCheckedAsParent: Boolean) {
                //선택한 댓글이 부모로 설정되었으면 부모로 저장
                if(isCheckedAsParent){
                    tv_reply_to.text = comment.mWriter;
                    tv_reply_to.visibility = View.VISIBLE;
                    mParentComment = comment.copy();
                }else{
                    tv_reply_to.visibility = View.INVISIBLE;
                }
            }
        })
        //닉네임 적는 란 비활성화
        tv_user_nickname.setText(player.mNickname);
        tv_user_nickname.isEnabled = false;
        tv_user_nickname.setTextColor(resources.getColor(R.color.disabled))

        mCommentViewModel.mComments.observe(this, androidx.lifecycle.Observer {
            mCommentAdapter.setComments(it);
        })
        mCommentViewModel.getPictureComment(pictureModel.mId);


        btn_submit.setOnClickListener {
            val date = Calendar.getInstance().time;
            val locale = requireContext().applicationContext.resources.configuration.locale;
            val comment = Comment(
                0, tv_user_nickname.text.toString(), player.mEmail, et_content.text.toString(),
                SimpleDateFormat("yyyy.MM.dd HH:mm", locale).format(date), pictureModel.mId, pictureModel.mOwnerEmail)
            mCommentViewModel.insertPictureComment(comment);
            //작성 후 덧글창 비우기
            et_content.setText("");
        }


    }


}