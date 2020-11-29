package com.manta.worldcup.activity.fragment.dialog



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants.EXTRA_PICTURE_NAMES
import com.manta.worldcup.helper.Constants.EXTRA_SUBMIT_LISTENER
import kotlinx.android.synthetic.main.dialog_picture_desc.*
import kotlinx.android.synthetic.main.dialog_picture_desc.view.*
import java.io.Serializable

class PictureDescriptionDialog() : DialogFragment() {


    interface OnSubmitListener : Serializable{
        fun onSubmit(pictureName : String)
    }


    fun newInstance(pictureNames : ArrayList<String>, onSubmitListener : OnSubmitListener):PictureDescriptionDialog {
        val args = Bundle(2)
        args.putStringArrayList(EXTRA_PICTURE_NAMES, pictureNames);
        args.putSerializable(EXTRA_SUBMIT_LISTENER, onSubmitListener);
        val fragment = PictureDescriptionDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_picture_desc, container, false);

        val onSubmitListener = arguments?.getSerializable(EXTRA_SUBMIT_LISTENER) as? OnSubmitListener ?: return view;
        val pictureNames = arguments?.getStringArrayList(EXTRA_PICTURE_NAMES) ?: return view;

        view.et_title.addTextChangedListener {
            if(pictureNames.contains(view.et_title.text.toString()))
                tv_title_warning.visibility = View.VISIBLE;
            else{
                tv_title_warning.visibility = View.INVISIBLE;
            }
        }

        view.btn_submit.setOnClickListener {
            val pictureName = view.et_title.text.toString()
            if(pictureNames.contains(pictureName))
                tv_title_warning.visibility = View.VISIBLE;
            else{
                tv_title_warning.visibility = View.INVISIBLE;
                onSubmitListener?.onSubmit(pictureName);
                dismiss();
            }
        }


        return view;
    }

}