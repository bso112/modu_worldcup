package com.manta.worldcup.activity.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manta.worldcup.R
import com.manta.worldcup.activity.MainActivity
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.User
import com.manta.worldcup.viewmodel.PictureViewModel
import com.manta.worldcup.viewmodel.TopicViewModel
import com.manta.worldcup.viewmodel.UserViewModel
import com.skydoves.balloon.*
import kotlinx.android.synthetic.main.dialog_profile.*
import kotlinx.android.synthetic.main.dialog_profile.iv_profile
import kotlinx.android.synthetic.main.dialog_profile.iv_tier
import kotlinx.android.synthetic.main.dialog_profile.tv_point


/**
 * 유저 정보를 보여주는 다이어로그
 */
class ProfileDialog : DialogFragment() {

    //현재 로그인한 유저에 대한 정보를 가지고 오기 위한 뷰모델
    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserViewModel(requireActivity().application) as T;
            }

        }).get(UserViewModel::class.java);
    }


    //유저가 올린 사진 수를 가지고 오기 위한 뷰모델
    private val mPictureViewModel: PictureViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PictureViewModel(requireActivity().application) as T;
            }

        }).get(PictureViewModel::class.java);
    }

    //내 토픽 갯수를 가져오기 위한 뷰모델
    private val mMyTopicViewModel: TopicViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopicViewModel(requireActivity().application) as T;
            }
        }).get(TopicViewModel::class.java);
    }

    private val REQUEST_PICK_FROM_ALBUM = 0

    companion object {
        fun newInstance(userWhoHasInfo: User): ProfileDialog {
            val args = Bundle(1)
            args.putSerializable(Constants.EXTRA_USER, userWhoHasInfo)
            val fragment = ProfileDialog()
            fragment.arguments = args
            return fragment
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_profile, container, false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //변경사항 적용
        (requireActivity() as? MainActivity)?.updateUser()
        (requireActivity() as? MainActivity)?.updateTopic()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //유저 정보로 표시할 유저
        val userWhoHasInfo = requireArguments().getSerializable(Constants.EXTRA_USER) as? User ?: return;
        //현재 로그인한 유저
        val userWhoSignIn = mUserViewModel.mUser.value ?: return;

        iv_set_picture.visibility = View.GONE
        iv_set_nickname.visibility = View.GONE
        //내 정보라면 유저 정보를 수정할 수 있게한다.
        if (userWhoHasInfo == userWhoSignIn) {
            iv_set_picture.visibility = View.VISIBLE
            iv_set_nickname.visibility = View.VISIBLE

            //프로필 사진 변경
            iv_profile.setOnClickListener {
                CreateProfilePictureSelectDialog().show()
            }

            //닉네임변경
            iv_set_nickname.setOnClickListener {
                ChangeNicknameDialog().newInstance(object : ChangeNicknameDialog.OnSubmitListener {
                    override fun onSubmit(nickname: String) {
                        mUserViewModel.updateUserNickname(nickname) {
                            tv_name.text = nickname;

                        };
                    }
                }).show(requireFragmentManager(), null);
            }
        }

        //프로필사진 셋팅
        if (userWhoHasInfo.mProfileImgName == null)
            iv_profile.setImageResource(R.drawable.ic_baseline_account_circle_24)
        else {
            val url = Constants.BASE_URL + "profile_image/get/" + userWhoHasInfo.mProfileImgName
            Constants.GlideWithHeader(url, iv_profile, iv_profile, requireContext());
        }

        //유저 정보 셋팅
        tv_name.text = userWhoHasInfo.mNickname;
        tv_point.text = userWhoHasInfo.mCurrPoint.toString();

        val tier = Constants.getTierIconID(userWhoHasInfo.mTier);
        tier?.let { _tier -> iv_tier.setImageResource(_tier) };

        mMyTopicViewModel.mDataset.observe(this, Observer {
            tv_num_worldcup.text = it.size.toString();
        })
        mMyTopicViewModel.getTopics(userWhoHasInfo.mEmail);

        mPictureViewModel.mPictures.observe(this, Observer {
            tv_num_picture.text = it.size.toString()
        })
        mPictureViewModel.getPictures(userWhoHasInfo.mEmail)

        var typedValue = TypedValue();
        requireContext().theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        val colorBackground = ContextCompat.getColor(requireContext(), typedValue.resourceId)

        requireContext().theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        val colorTextColor = ContextCompat.getColor(requireContext(), typedValue.resourceId)

        btn_help_tier.setOnClickListener {
            val balloon: Balloon = Balloon.Builder(requireContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                .setPadding(6)
                .setArrowPosition(0.5f)
                .setCornerRadius(10f)
                .setLayout(R.layout.dialog_help_tier)
                .setBackgroundColor(colorBackground)
                .setTextColor(colorTextColor)
                .setText(resources.getString(R.string.info_add_picture))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build()

            balloon.show(btn_help_tier)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_PICK_FROM_ALBUM) {
            val bitmapList = BitmapHelper.getBitmapFromIntent(data, requireContext().contentResolver, Intent.ACTION_GET_CONTENT);
            if (bitmapList.isNotEmpty()) {
                iv_profile.setImageBitmap(bitmapList.first())
                mUserViewModel.uploadProfileImage(bitmapList.first())

            }
        }
    }

    private fun CreateProfilePictureSelectDialog(): Dialog {
        return AlertDialog.Builder(requireContext())
            .setItems(R.array.option_profile_picture, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        //갤러리에서 사진 가져오기
                        pickPictureFromGallay()
                    }
                    1 -> {
                        //기본이미지로 대체
                        iv_profile.setImageResource(R.drawable.ic_baseline_account_circle_24)
                        mUserViewModel.removeProfileImage()
                    }

                }
            })
            .create()
    }

    private fun pickPictureFromGallay() {
        Intent(Intent.ACTION_GET_CONTENT).let {
            it.type = "image/*"
            startActivityForResult(Intent.createChooser(it, "Select Picture"), REQUEST_PICK_FROM_ALBUM)
        }
    }

}

