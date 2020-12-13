package com.manta.worldcup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureCommentDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.User
import kotlinx.android.synthetic.main.item_my_picture.view.*
import kotlin.collections.ArrayList
import kotlin.math.floor

/**
 * item_my_picture를 보여주는 리사이클러뷰 어댑터
 * @author 변성욱
 * @param mFragementManager picture에 달린 댓글을 보여줄 fragment를 호스팅하는 액티비티의 fragementManager
 */
class MyPictureAdapter(val mFragementManager : FragmentManager) : RecyclerView.Adapter<MyPictureAdapter.MyPictureViewHolder>() {

    private var mDataset: ArrayList<PictureModel> = ArrayList();
    private var mUser : User? = null;

    inner class MyPictureViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mPictureView: ImageView = view.iv_picture;
        val mWinCnt: TextView = view.tv_winCnt;
        val mCommentBtn : ImageButton = view.btn_comment

        fun setPicture(picture: PictureModel) {
            Glide.with(view.context).load(Constants.BASE_URL + "image/get/${picture.mId}").into(mPictureView);
            mWinCnt.text = if(picture.WinCnt >= 1000) "${floor(picture.WinCnt / 100.0F) / 10.0F}K" else picture.WinCnt.toString();
            mCommentBtn.setOnClickListener {
                if(mUser != null)
                    PictureCommentDialog().newInstance(picture, mUser!!).show(mFragementManager, null);
            }
        }
    }

    class PictureDiffUtill(private val oldList: List<PictureModel>, private val newList: List<PictureModel>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].mId == newList[newItemPosition].mId;
        }

        override fun getOldListSize(): Int {
            return oldList.size;
        }

        override fun getNewListSize(): Int {
            return newList.size;
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition];
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_picture, parent, false)
        return MyPictureViewHolder(view);
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(holder: MyPictureViewHolder, position: Int) {
        holder.setPicture(mDataset[position]);
    }

    fun setPictures(pictures : ArrayList<PictureModel>){
        val result = DiffUtil.calculateDiff(PictureDiffUtill(mDataset, pictures))
        mDataset = pictures;
        result.dispatchUpdatesTo(this);
    }

    /**
     * 댓글을 달때 참조하기 위한 유저데이터
     * @author 변성욱
     * @param mUser 현재 접속된 계정의 유저데이터
     */
    fun setUser(user : User?){
        mUser = user;
    }

}