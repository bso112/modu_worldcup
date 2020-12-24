package com.manta.worldcup.activity.fragment.dialog



import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.helper.Constants.EXTRA_PICTURE_NAME
import com.manta.worldcup.helper.Constants.EXTRA_PICTURE_NAMES
import com.manta.worldcup.helper.Constants.EXTRA_SUBMIT_LISTENER
import kotlinx.android.synthetic.main.dialog_picture_desc.*
import kotlinx.android.synthetic.main.dialog_picture_desc.view.*
import java.io.Serializable

/**
 * 토픽을 만들때나 토픽에 사진을 추가할때 사진을 누르면
 * 사진의 이름을 정할 수 있다. 그때 띄워주는 다이어로그다.
 * @author 변성욱
 */
class PictureDescriptionDialog() : DialogFragment() {


    interface OnSubmitListener : Serializable{
        fun onSubmit(pictureName : String)
    }


    fun newInstance(pictureNames : ArrayList<String>,
                    currentPictureName : String?,
                    onSubmitListener : OnSubmitListener):PictureDescriptionDialog {
        val args = Bundle(3)
        args.putStringArrayList(EXTRA_PICTURE_NAMES, pictureNames);
        args.putString(EXTRA_PICTURE_NAME, currentPictureName)
        args.putSerializable(EXTRA_SUBMIT_LISTENER, onSubmitListener);
        val fragment = PictureDescriptionDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_picture_desc, container, false);

        val onSubmitListener = arguments?.getSerializable(EXTRA_SUBMIT_LISTENER) as? OnSubmitListener ?: return view;
        val pictureNames = arguments?.getStringArrayList(EXTRA_PICTURE_NAMES) ?: return view;
        val currentPictureName = arguments?.getString(EXTRA_PICTURE_NAME) ?: "";

        view.et_title.setText(currentPictureName);

        //중복검사
        view.et_title.addTextChangedListener {
            if(IsNameDuplicated(pictureNames, currentPictureName))
                tv_title_warning.visibility = View.VISIBLE;
            else{
                tv_title_warning.visibility = View.INVISIBLE;
            }
        }

        view.btn_submit.setOnClickListener {
            val pictureName = view.et_title.text.toString()
            if(IsNameDuplicated(pictureNames, currentPictureName))
                tv_title_warning.visibility = View.VISIBLE;
            else{
                //키보드 내리기
                val inputManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.et_title.windowToken, 0)
                tv_title_warning.visibility = View.INVISIBLE;
                onSubmitListener.onSubmit(pictureName);
                dismiss();
            }
        }


        return view;
    }

    private fun IsNameDuplicated(pictureNames : ArrayList<String>, currentPictureName: String?) : Boolean{
        val pictureName = et_title.text.toString()
        return pictureName != "" && pictureNames.contains(pictureName) && pictureName != currentPictureName
    }

}