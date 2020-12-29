package com.manta.worldcup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureInfoDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.User
import kotlinx.android.synthetic.main.item_statistic_picture.view.*
import org.w3c.dom.Text
import kotlin.collections.ArrayList
import kotlin.math.floor

/**
 * 내가 올린 사진(item_my_picture)을 보여주는 어댑터
 * @author 변성욱
 * @param mFragementManager picture에 달린 댓글을 보여줄 fragment를 호스팅하는 액티비티의 fragementManager
 */
class StatisticPictureAdpater(
    private var mFragementManager: FragmentManager?,
    private val mNotifiedPictureId: String? = null
) : RecyclerView.Adapter<StatisticPictureAdpater.MyPictureViewHolder>() {

    private var mDataset: ArrayList<PictureModel> = ArrayList();

    //현재 로그인한 유저
    private var mUser: User? = null;
    private lateinit var mContext: Context;


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mContext = recyclerView.context;
    }


    inner class MyPictureViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mPictureView: ImageView = view.iv_picture;
        val mPictureName: TextView = view.tv_picture_name
        val mWinCnt: TextView = view.tv_winCnt;
        val mWinPercent: TextView = view.tv_percent;
        val mRanking: TextView = view.tv_ranking;
        val mCrown: ImageView = view.iv_crown;

        fun setPicture(picture: PictureModel) {
            Glide.with(view.context).load(Constants.BASE_URL + "image/get/${picture.mId}").into(mPictureView);
            mWinPercent.text = "${getWinPercent(picture).toString()}%";
            val resource = view.context.resources;
            mWinCnt.text =
                if (picture.mWinCnt >= 1000)
                    "(${floor(picture.mWinCnt / 100.0F) / 10.0F}K${resource.getString(R.string.people)})"
                else
                    "(${picture.mWinCnt}${resource.getString(R.string.people)})";

            mRanking.text = "${adapterPosition + 1}" + resource.getString(R.string.place)
            if (adapterPosition == 0)
                mCrown.visibility = View.VISIBLE;
            else
                mCrown.visibility = View.GONE;

            mPictureName.text = picture.mPictureName
        }

        private fun getWinPercent(picture: PictureModel): Int {
            var sumWin = 0
            for (data in mDataset) {
                sumWin += data.mWinCnt
            }
            return if (sumWin == 0) 0 else (picture.mWinCnt.toFloat() / sumWin.toFloat() * 100).toInt()
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistic_picture, parent, false)
        return MyPictureViewHolder(view);
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(holder: MyPictureViewHolder, position: Int) {
        holder.setPicture(mDataset[position]);
    }

    fun setPictures(pictures: ArrayList<PictureModel>, isSortByWinCnt: Boolean = false) {
        var sortedList: ArrayList<PictureModel> = pictures
        if (isSortByWinCnt) {
            sortedList = sortByWinCnt(pictures)
        }
        val result = DiffUtil.calculateDiff(PictureDiffUtill(mDataset, sortedList))
        mDataset = sortedList;
        result.dispatchUpdatesTo(this);
    }

    /**
     * 댓글을 달때 참조하기 위한 유저데이터
     * @author 변성욱
     * @param mUser 현재 접속된 계정의 유저데이터
     */
    fun setUser(user: User?) {
        mUser = user;
    }


    private fun sortByWinCnt(pictureModels: List<PictureModel>) = ArrayList(pictureModels.sortedByDescending { it.mWinCnt })


}