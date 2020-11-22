package com.manta.worldcup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.model.Topic
import kotlinx.android.synthetic.main.item_topic.view.*
import kotlin.collections.ArrayList

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private var mDataset : List<Topic> = ArrayList();
    private var mOnItemClickListener : OnItemClickListener? = null;

    interface OnItemClickListener{
        fun onItemClick(note : Topic);
    }


    class TopicDiffUtilCallback(private val oldList : List<Topic>, private val newList : List<Topic>) : DiffUtil.Callback(){
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id;
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

    inner class TopicViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val mTumbnail : ImageView = view.iv_thumbnail;
        val mTitle : TextView = view.tv_title;
        val mManagerName : TextView = view.tv_managerName;


        init {
            view.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION)
                    mOnItemClickListener?.onItemClick(mDataset.get(adapterPosition));
            }
        }

        fun setTopic(topic : Topic){
            if(topic.mPictures.isNotEmpty())
                mTumbnail.setImageBitmap(topic.mPictures.first().mBitmap)
            mTitle.text = topic.title;
            mManagerName.text = topic.mManager.mNickname;
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
        if(mDataset.size > position)
            holder.setTopic(mDataset[position]);
    }

    fun setTopics(topics : List<Topic>){
        val result = DiffUtil.calculateDiff(TopicDiffUtilCallback(mDataset, topics));
        mDataset = topics;
        result.dispatchUpdatesTo(this);
    }

    fun setOnItemClickListener(listenr : OnItemClickListener){
        mOnItemClickListener = listenr;
    }

}