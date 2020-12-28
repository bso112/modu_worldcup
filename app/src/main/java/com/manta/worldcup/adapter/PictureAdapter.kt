package com.manta.worldcup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import kotlinx.android.synthetic.main.item_my_picture.view.*
import kotlin.collections.ArrayList
import kotlin.math.floor

/**
 * 내가 올린 사진(item_my_picture)을 보여주는 어댑터
 * @author 변성욱
 * @param mFragementManager picture에 달린 댓글을 보여줄 fragment를 호스팅하는 액티비티의 fragementManager
 */
class PictureAdapter(
    private var mFragementManager: FragmentManager?,
    private val mNotifiedPictureId: String? = null
) : RecyclerView.Adapter<PictureAdapter.MyPictureViewHolder>() {

    private var mDataset: ArrayList<PictureModel> = ArrayList();

    //사진의 노티피케이션 정보를 담은 리스트
    private var mNotification = MutableList(0) { false }

    //사진의 선택정보를 담은 리스트
    private var mSelected = MutableList(0) { false }

    //삭제할 사진을 선택하는 모드인가?
    private var mIsSelectMode = false;

    //현재 로그인한 유저
    private var mUser: User? = null;
    private lateinit var mContext: Context;

    //mNotifiedTopicId 에 해당하는 뷰의 애니메이션이 처음 실행되는 상태인가?
    private var mIsFirstAnimation: Boolean = true;

    private var mOnItemLongClick: OnItemLongClickListener? = null

    interface OnItemLongClickListener {
        fun onItemLongClick();
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mContext = recyclerView.context;
    }


    inner class MyPictureViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mPictureView: ImageView = view.iv_picture;
        val mWinCnt: TextView = view.tv_winCnt;
        val mNotificationBadge = view.iv_notification;
        val mCheckedMark: ImageView = view.iv_checked;

        init {
            view.setOnClickListener {
                //사진을 선택하는 모드면 사진 선택만함.
                if (mIsSelectMode) {
                    mSelected[adapterPosition] = !mSelected[adapterPosition];
                    notifyItemChanged(adapterPosition);
                    return@setOnClickListener
                }

                if (mUser != null && mFragementManager != null)
                    PictureInfoDialog()
                        .newInstance(
                        mDataset[adapterPosition],
                        mUser!!,
                        object : PictureInfoDialog.OnPictureNameChangeListener {
                            override fun onPictureNameChange(pictureName: String) {
                                mDataset[adapterPosition].mPictureName = pictureName;
                            }
                        })
                        .show(mFragementManager!!, null);

                if (mNotificationBadge.visibility == View.VISIBLE) {
                    mNotificationBadge.visibility = View.INVISIBLE;
                    mNotification[adapterPosition] = false;

                    //노피티케이션 확인했으니 삭제.
                    val pref = mContext.applicationContext.getSharedPreferences(Constants.PREF_FILE_NOTIFICATION, Context.MODE_PRIVATE)
                    val pictureNotification = pref.getStringSet(Constants.PREF_NOTIFIED_PICTURE_ID, emptySet());
                    pictureNotification?.remove(mDataset[adapterPosition].mId.toString())
                    pref.edit().putStringSet(Constants.PREF_NOTIFIED_PICTURE_ID, pictureNotification).apply();
                }
            }

            view.setOnLongClickListener {
                mSelected[adapterPosition] = true;
                notifyItemChanged(adapterPosition);
                if (!mIsSelectMode) {
                    mIsSelectMode = true;
                    mOnItemLongClick?.onItemLongClick();
                }
                true;
            }

        }

        fun setPicture(picture: PictureModel) {
            Glide.with(view.context).load(Constants.BASE_URL + "image/get/${picture.mId}").into(mPictureView);
            mWinCnt.text = if (picture.WinCnt >= 1000) "${floor(picture.WinCnt / 100.0F) / 10.0F}K" else picture.WinCnt.toString();

            //노티피케이션 확인
            if (mNotification[adapterPosition])
                mNotificationBadge.visibility = View.VISIBLE;
            //이거 안해주면 이 뷰홀더의 mNotificationBadge는 VISIBLE이고, 그대로 재활용됨.
            else
                mNotificationBadge.visibility = View.INVISIBLE;

            if (mNotifiedPictureId != null && mNotifiedPictureId == picture.mId.toString() && mIsFirstAnimation) {
                view.requestFocus()
                val anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_shake)
                view.startAnimation(anim)
                mIsFirstAnimation = false;
            }

            if (mSelected[adapterPosition]) {
                mCheckedMark.visibility = View.VISIBLE;
            } else
                mCheckedMark.visibility = View.INVISIBLE;

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
        mSelected = MutableList(mDataset.size) { false };
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

    fun isSelectMode() = mIsSelectMode;

    fun selectAll() {
        mSelected.fill(true)
        notifyDataSetChanged()
    }

    fun getSelection(): MutableList<PictureModel> {
        val selected = mutableListOf<PictureModel>()
        for (i in mDataset.indices) {
            if (mSelected[i]) {
                selected.add(mDataset[i])
            }
        }
        return selected;
    }

    fun endSelectMode() {
        mIsSelectMode = false;
        mSelected.fill(false)
        notifyDataSetChanged()
    }

    fun setOnLongClickListener(listener: OnItemLongClickListener) {
        mOnItemLongClick = listener;
    }

    fun notifyItemDeleted(pictureModels: List<PictureModel>) {
        for (picture in pictureModels) {
            val iter = mDataset.listIterator();
            while (iter.hasNext()) {
                val index = iter.nextIndex();
                val next = iter.next()
                if (picture.mId == next.mId) {
                    iter.remove();
                    notifyItemRemoved(index);
                }
            }
        }

    }

}