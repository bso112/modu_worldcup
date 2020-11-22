package com.manta.worldcup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.dataclass.Topic
import kotlinx.android.synthetic.main.item_topic.view.*
import java.util.*
import kotlin.collections.ArrayList

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private var mDataset : List<Topic> = ArrayList();

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

    class TopicViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val mTumbnail : ImageView = view.iv_thumbnail;
        val mTitle : TextView = view.tv_title;
        val mManagerName : TextView = view.tv_managerName;

        fun setTopic(topic : Topic){
            if(topic.mPictures.isNotEmpty())
                mTumbnail.setImageBitmap(topic.mPictures.first().mBitmap)
            mTitle.text = topic.title;
            mManagerName.text = topic.mManager.mNickname;
        }
    }

    //parent는 뭘까? 어댑터에 연결된 리사이클러뷰인가? attachToRoot를 true로 하는거랑 false로 하는거의 차이는 눈에보이나?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, true);
        return TopicViewHolder(view);
    }

    override fun getItemCount(): Int {
        return mDataset.size;
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        if(mDataset.size > position)
            holder.setTopic(mDataset[position]);
    }

    fun SetTopics(topics : List<Topic>){
        val result = DiffUtil.calculateDiff(TopicDiffUtilCallback(mDataset, topics));
        mDataset = topics;
        result.dispatchUpdatesTo(this);
    }


}