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


    fun newInstance(picture: PictureModel, player: User): PictureCommentDialog {
        val args = Bundle(2);
        args.putSerializable(Constants.EXTRA_PICTUREMODEL, picture);
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

        val pictureModel = requireArguments().getSerializable(Constants.EXTRA_PICTUREMODEL) as? PictureModel ?: return;
        val player = requireArguments().getSerializable(Constants.EXTRA_USER) as? User ?: return;

        rv_comment.adapter = mCommentAdapter;
        rv_comment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        //닉네임 적는 란 비활성화
        et_nickname.setText(player.mNickname);
        et_nickname.isEnabled = false;
        et_nickname.setTextColor(resources.getColor(R.color.disabled))

        mCommentViewModel.mComments.observe(this, androidx.lifecycle.Observer {
            mCommentAdapter.setComments(it);
        })
        mCommentViewModel.getPictureComment(pictureModel.mId);


        btn_submit.setOnClickListener {
            val date = Calendar.getInstance().time;
            val locale = requireContext().applicationContext.resources.configuration.locale;
            val comment = Comment(
                0, et_nickname.text.toString(), et_content.text.toString(),
                SimpleDateFormat("yyyy.MM.dd HH:mm", locale).format(date), pictureModel.mId
            )
            mCommentViewModel.insertPictureComment(comment);
            //작성 후 덧글창 비우기
            et_content.setText("");
        }

    }


}