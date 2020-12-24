package com.manta.worldcup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureInfoDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.User
import kotlinx.android.synthetic.main.item_my_picture.view.*
import kotlin.collections.ArrayList
import kotlin.math.floor

/**
 * 타인이 올린 사진을 나타내는 어댑터
 * @author 변성욱
 */
class PictureAdapter() : RecyclerView.Adapter<PictureAdapter.MyPictureViewHolder>() {

    private var mDataset: ArrayList<PictureModel> = ArrayList();
    private var mNotification = MutableList(0) { false }
    private var mUser: User? = null;
    private lateinit var mContext : Context;
    private lateinit var mFragementManager : FragmentManager;

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context;
        mFragementManager =  (mContext as AppCompatActivity).supportFragmentManager;
    }

    inner class MyPictureViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mPictureView: ImageView = view.iv_picture;
        val mWinCnt: TextView = view.tv_winCnt;

        init {
            mPictureView.setOnClickListener {
                if (mUser != null)
                    PictureInfoDialog().newInstance(mDataset[adapterPosition], mUser!!).show(mFragementManager, null);

            }
        }

        fun setPicture(picture: PictureModel) {
            Glide.with(view.context).load(Constants.BASE_URL + "image/get/${picture.mId}").into(mPictureView);
            mWinCnt.text = if (picture.WinCnt >= 1000) "${floor(picture.WinCnt / 100.0F) / 10.0F}K" else picture.WinCnt.toString();
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

    fun setPictures(pictures: ArrayList<PictureModel>) {
        val result = DiffUtil.calculateDiff(PictureDiffUtill(mDataset, pictures))
        mDataset = pictures;
        mNotification = MutableList(mDataset.size) { false };
        result.dispatchUpdatesTo(this);
        //알림표시
        val pref = mContext.applicationContext.getSharedPreferences(Constants.PREF_FILE_NOTIFICATION, Context.MODE_PRIVATE)
        val notifiedPictureID = pref.getStringSet(Constants.PREF_NOTIFIED_PICTURE_ID, HashSet());
        if (notifiedPictureID!!.isNotEmpty()) {
            for (i in 0 until mDataset.size) {
                if (notifiedPictureID.contains(mDataset[i].mId.toString())) {
                    mNotification[i] = true;
                    notifyItemChanged(i);
                } else
                    mNotification[i] = false;
            }
        }
    }

    /**
     * 댓글을 달때 참조하기 위한 유저데이터
     * @author 변성욱
     * @param mUser 현재 접속된 계정의 유저데이터
     */
    fun setUser(user: User?) {
        mUser = user;
    }


}