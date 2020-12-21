package com.manta.worldcup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import kotlinx.android.synthetic.main.item_topic.view.*
import kotlin.collections.ArrayList

/**
 * item_topic 을 보여주는 리사이클러뷰 어댑터
 * @author 변성욱
 */
class MyTopicAdapter() : RecyclerView.Adapter<MyTopicAdapter.TopicViewHolder>() {

    private var mDataset: List<TopicJoinUser> = ArrayList();
    private var mOnItemClickListener: OnItemClickListener? = null;
    private var mNotifiedTopicIDs = emptySet<String>()
    private lateinit var mContext : Context;


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

        val mTumbnail: ImageView = view.iv_thumbnail;
        val mTitle: TextView = view.tv_title;
        val mManagerName: TextView = view.tv_managerName;
        val mNotifyBadge = view.iv_notification;

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    mOnItemClickListener?.onItemClick(mDataset.get(adapterPosition));
                    //노티피케이션이 있으면
                    if (mNotifyBadge.visibility == View.VISIBLE) {
                        //노피티케이션 확인했으니 삭제.
                        val pref = mContext.applicationContext.getSharedPreferences(Constants.PREF_FILE_NOTIFICATION, Context.MODE_PRIVATE)
                        val topicNotifications = pref.getStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, emptySet());
                        topicNotifications?.remove("${mDataset.get(adapterPosition).mId}")
                        pref.edit().putStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, topicNotifications).apply();
                        mNotifyBadge.visibility = View.INVISIBLE
                    }
                }
            }
        }

        fun setTopic(data: TopicJoinUser) {

            mContext?.let {
                val urlToPicture = Constants.BASE_URL + "image/get/${data.mId}/0";
                Constants.GlideWithHeader(urlToPicture, view, mTumbnail, it);
            }

            mTitle.text = data.mTitle;
            mManagerName.text = data.mManagerName;

            val tierIconID = Constants.getTierIconID(data.mTier);
            if (tierIconID != null)
                view.iv_tier.setImageResource(tierIconID);

            //덧글달린 토픽표시
            if (mNotifiedTopicIDs.contains("${data.mId}"))
                mNotifyBadge.visibility = View.VISIBLE;
            else
                mNotifyBadge.visibility = View.INVISIBLE;
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

    fun setNotification(notifiedTopicIDs: Set<String>) {
        mNotifiedTopicIDs = notifiedTopicIDs
        notifyDataSetChanged();
    }

    fun setOnItemClickListener(listenr: OnItemClickListener) {
        mOnItemClickListener = listenr;
    }



}