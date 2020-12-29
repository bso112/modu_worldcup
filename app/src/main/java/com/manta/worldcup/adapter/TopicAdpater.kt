package com.manta.worldcup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.TopicOptionDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import kotlinx.android.synthetic.main.item_topic.view.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

/**
 * item_topic4 을 보여주는 리사이클러뷰 어댑터 (노티피케이션 없이)
 * @author 변성욱
 */
class TopicAdpater() : RecyclerView.Adapter<TopicAdpater.TopicViewHolder>() {

    private var mDataset: List<TopicJoinUser> = ArrayList();
    private var mOnItemClickListener: OnItemClickListener? = null;
    private lateinit var mContext: Context;


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context;
    }


    interface OnItemClickListener {
        fun onItemClick(topicJoinUser: TopicJoinUser);
    }


    class TopicDiffUtilCallback(private val oldList: List<TopicJoinUser>, private val newList: List<TopicJoinUser>) : DiffUtil.Callback() {
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


    inner class TopicViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val mFirstImg: ImageView = view.iv_first;
        val mSecondImg: ImageView = view.iv_second;
        val mTitle: TextView = view.tv_title;
        val mManagerName: TextView = view.tv_managerName;
        val mDate: TextView = view.tv_date;
        val mTier: ImageView = view.iv_tier
        val mOptionBtn: ImageButton = view.btn_more


        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    mOnItemClickListener?.onItemClick(mDataset.get(adapterPosition));

                }
            }

            mOptionBtn.setOnClickListener {
                TopicOptionDialog().show((mContext as AppCompatActivity).supportFragmentManager, null);
            }
        }


        fun setTopic(data: TopicJoinUser) {

            mContext.let {
                //이미지가 서버에 없더라도 기기에 캐싱되어서 화면에 보여지기 때문에 imageLength로 판단해줘야함.
                val isUseCache = data.mImageLength > 2
                var urlToPicture = Constants.BASE_URL + "image/get/${data.mId}/0";
                Constants.GlideWithHeader(urlToPicture, view, mFirstImg, it, isUseCache);
                urlToPicture = Constants.BASE_URL + "image/get/${data.mId}/1";
                Constants.GlideWithHeader(urlToPicture, view, mSecondImg, it, isUseCache);
            }

            mTitle.text = data.mTitle;
            mManagerName.text = data.mManagerName;
            //시간 설정
            val locale = view.context.applicationContext.resources.configuration.locale;
            val transFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", locale);
            val date = transFormat.parse(data.mDate)
            date?.let {
                val timePassed = Constants.getTimePassedFromNow(view.context.resources, it)
                mDate.text = timePassed
            }

            val tierIconID = Constants.getTierIconID(data.mTier);
            if (tierIconID != null)
                mTier.setImageResource(tierIconID);

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false);
        return TopicViewHolder(view);
    }

    override fun getItemCount(): Int {
        return mDataset.size;
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        if (mDataset.size > position)
            holder.setTopic(mDataset[position]);
    }

    fun setTopics(topics: List<TopicJoinUser>) {
        val result = DiffUtil.calculateDiff(TopicDiffUtilCallback(mDataset, topics));
        mDataset = topics;
        result.dispatchUpdatesTo(this);

    }


    fun setOnItemClickListener(listenr: OnItemClickListener) {
        mOnItemClickListener = listenr;
    }

}