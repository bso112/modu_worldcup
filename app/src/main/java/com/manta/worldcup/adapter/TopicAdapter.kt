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
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.model.User
import kotlinx.android.synthetic.main.item_topic.view.*
import kotlin.collections.ArrayList

class TopicAdapter(private val mContext: Context) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private var mDataset: List<TopicJoinUser> = ArrayList();
    private var mOnItemClickListener: OnItemClickListener? = null;


    interface OnItemClickListener {
        fun onItemClick(topicJoinUser : TopicJoinUser);
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


        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    mOnItemClickListener?.onItemClick(mDataset.get(adapterPosition));
            }
        }

        fun setTopic(data : TopicJoinUser) {

            val urlToPicture = Constants.BASE_URL + "image/get/${data.mId}/0";
            Constants.GlideWithHeader(urlToPicture, view, mTumbnail, mContext);

            mTitle.text = data.mTitle;
            mManagerName.text = data.mManagerName;

            val tierIconID = Constants.getTierIconID(data.mTier);
            if(tierIconID != null)
                view.iv_tier.setImageResource(tierIconID);
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