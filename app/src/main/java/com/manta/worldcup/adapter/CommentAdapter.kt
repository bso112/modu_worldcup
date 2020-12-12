package com.manta.worldcup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter() : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var mDataset : ArrayList<Comment> =  ArrayList();

    class CommentViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val mNickname = view.et_nickname;
        val mContent = view.tv_content;
        val mDate = view.tv_date;

        fun setComment(comment : Comment){
            mNickname.text = comment.mWriter;
            mContent.text = comment.mContents;
            mDate.text = comment.mDate.toString();
        }
    }

    class CommentDiffUtilCallback(private val oldList: List<Comment>, private val newList: List<Comment>) : DiffUtil.Callback() {
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false);
        return CommentViewHolder(view);
    }


    override fun getItemCount(): Int {
        return mDataset.size;
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.setComment(mDataset[position]);
    }

    fun setComments(comments : ArrayList<Comment>){
        val result = DiffUtil.calculateDiff(CommentDiffUtilCallback(mDataset, comments))
        mDataset = comments;
        result.dispatchUpdatesTo(this)
    }

}
