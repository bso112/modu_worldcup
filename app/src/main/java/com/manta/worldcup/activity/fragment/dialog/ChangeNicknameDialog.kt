package com.manta.worldcup.activity.fragment.dialog



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants.EXTRA_SUBMIT_LISTENER
import kotlinx.android.synthetic.main.dialog_change_nickname.*
import kotlinx.android.synthetic.main.dialog_change_nickname.view.*
import java.io.Serializable

class ChangeNicknameDialog() : DialogFragment() {


    interface OnSubmitListener : Serializable{
        fun onSubmit(nickname : String)
    }


    fun newInstance(onSubmitListener : OnSubmitListener):ChangeNicknameDialog {
        val args = Bundle(2)
        args.putSerializable(EXTRA_SUBMIT_LISTENER, onSubmitListener);
        val fragment = ChangeNicknameDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_change_nickname, container, false);

        val onSubmitListener = arguments?.getSerializable(EXTRA_SUBMIT_LISTENER) as? OnSubmitListener ?: return view;

        view.btn_submit.setOnClickListener {
            onSubmitListener.onSubmit(tv_user_nickname.text.toString())
            dismiss()
        }

        return view;
    }

}