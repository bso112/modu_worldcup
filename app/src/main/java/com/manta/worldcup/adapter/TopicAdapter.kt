package com.manta.worldcup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicModel
import kotlinx.android.synthetic.main.item_topic.view.*
import kotlin.collections.ArrayList

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private var mDataset: List<TopicModel> = ArrayList();
    private var mOnItemClickListener: OnItemClickListener? = null;

    interface OnItemClickListener {
        fun onItemClick(note: TopicModel);
    }


    class TopicDiffUtilCallback(private val oldList: List<TopicModel>, private val newList: List<TopicModel>) : DiffUtil.Callback() {
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

        fun setTopic(topic: TopicModel) {
            Glide.with(view).load(Constants.BASE_URL + "picture/get/${topic.mId}/0").into(mTumbnail);
            //mTumbnail.setImageBitmap(topic.mPictures.first().mBitmap)

            mTitle.text = topic.mTitle;
            mManagerName.text = topic.mManagerName;
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

    fun setTopics(topics: List<TopicModel>) {
        val result = DiffUtil.calculateDiff(TopicDiffUtilCallback(mDataset, topics));
        mDataset = topics;
        result.dispatchUpdatesTo(this);


    }

    fun setOnItemClickListener(listenr: OnItemClickListener) {
        mOnItemClickListener = listenr;
    }

}