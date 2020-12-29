package com.manta.worldcup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import kotlinx.android.synthetic.main.item_topic_recent.view.*

class RecentTopicAdapter() : RecyclerView.Adapter<RecentTopicAdapter.PagerViewHolder>() {
    private var mDataset = emptyList<TopicJoinUser>()

    private var mOnTopicClick : OnTopicClickListener? = null;

    interface OnTopicClickListener{
        fun onTopicClick(topicJoinUser: TopicJoinUser);
    }

    inner class PagerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mtvTitle = view.tv_title
        val mtvManagerName = view.tv_managerName
        val mivTier = view.iv_tier
        val mivTumbnail = view.iv_thumbnail

        fun bindData(topicJoinUser: TopicJoinUser) {
            view.setOnClickListener {
                mOnTopicClick?.onTopicClick(topicJoinUser);
            }

            val urlToPicture = Constants.BASE_URL + "image/get/${topicJoinUser.mId}/0";
            Constants.GlideWithHeader(urlToPicture, view, mivTumbnail, view.context);

            mtvTitle.text = topicJoinUser.mTitle;
            mtvManagerName.text = topicJoinUser.mManagerName;

            val tierIconID = Constants.getTierIconID(topicJoinUser.mTier);
            if (tierIconID != null)
                mivTier.setImageResource(tierIconID);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_topic_recent, parent, false));
    }

    override fun getItemCount(): Int {
       return mDataset.size
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bindData(mDataset[position])
    }

    fun setTopicJoinUsers(topicJoinUser: List<TopicJoinUser>) {
        mDataset = topicJoinUser
        notifyDataSetChanged()
    }

    fun setOnTopicClickListener(listener : OnTopicClickListener){
        mOnTopicClick = listener
    }

}