package com.manta.worldcup.adapter

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class CommentAdapter() : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var mDataset: ArrayList<Comment> = ArrayList();

    //현재 추천버튼을 누른 댓글을 표시하기 위한 정보
    private var mIsRecommended = emptyArray<Boolean>()
    private var mOnItemClickListener: OnItemClickListener? = null;
    private var mOnRecommendBtnClickListener: OnRecommendBtnClickListener? = null;

    interface OnItemClickListener {
        fun onItemClick(comment: Comment);
    }

    interface OnRecommendBtnClickListener {
        fun onRecommend(commentID: Long)
    }

    inner class CommentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mNickname = view.tv_user_nickname;
        val mContent = view.tv_content;
        val mDate = view.tv_date;
        val mRecommendBtn = view.btn_like;
        val mtvRecommend = view.tv_Recommend;
        val mtvReplyMark = view.tv_reply_mark;

        init {
            mRecommendBtn.setOnClickListener {
                if (!mIsRecommended[adapterPosition]) {
                    mOnRecommendBtnClickListener?.onRecommend(mDataset[adapterPosition].mId)
                    mDataset[adapterPosition].mRecommend++;
                    mIsRecommended[adapterPosition] = true;
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        init {
            view.setOnClickListener {
                mOnItemClickListener?.onItemClick(mDataset[adapterPosition]);
            }
        }

        fun setComment(comment: Comment) {
            mNickname.text = comment.mWriter;
            mContent.text = comment.mContents;
            mDate.text = comment.mDate
            mtvRecommend.text = comment.mRecommend.toString();

            //대댓글인지 표시
            if (comment.mParentID != null)
                mtvReplyMark.visibility = View.VISIBLE
            else
                mtvReplyMark.visibility = View.GONE

            if (mIsRecommended[adapterPosition]) {
                var typedValue = TypedValue();
                view.context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
                val primaryColor = typedValue.data;
                mRecommendBtn.backgroundTintList = ColorStateList.valueOf(primaryColor)
            }


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

    fun setComments(comments: ArrayList<Comment>) {
        val nomalized = nomalizeComments(comments) ?: return
        val result = DiffUtil.calculateDiff(CommentDiffUtilCallback(mDataset, nomalized))
        mDataset = nomalized;
        mIsRecommended = Array(mDataset.size) { false }
        result.dispatchUpdatesTo(this)
    }

    //베스트댓글을 맨위로 올리고 대댓글 위치조정
    //대댓글이 베스트가 되는건 막는다.
    private fun nomalizeComments(comments: ArrayList<Comment>) : ArrayList<Comment>? {
        if (comments.isEmpty())
            return null;

        val result = LinkedList<Comment>();

        for (comment in comments) {
            if (comment.mParentID == null)
                result.add(comment)
        }

        var maxRecommend = 0
        var bestCommentIndex = 0
        for (index in 0 until result.size) {
            if (result[index].mParentID != null) continue;
            if (maxRecommend < result[index].mRecommend) {
                maxRecommend = result[index].mRecommend
                bestCommentIndex = index;
            }
        }
        //swap
        result[0] = result[bestCommentIndex].also { result[bestCommentIndex] = result[0] }


        //대댓글 위치조정
        //parent는 result에, 자식은 comment 에 있다.
        for (index in 0 until comments.size) {
            if (comments[index].mParentID != null) {
                val parentIndex = result.indexOfFirst { it.mId == comments[index].mParentID }
                if (parentIndex >= 0) {
                        result.add(min(parentIndex + 1, result.size), comments[index])
                }else{
                    val a = 4;
                }
            }
        }

        return ArrayList<Comment>(result);

    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener;
    }

    fun setOnRecommendBtnClickListener(listener: OnRecommendBtnClickListener) {
        mOnRecommendBtnClickListener = listener
    }

    /**
     * 재귀적으로 root 코멘트 (부모가 없는 코멘트)를 찾아준다.
     * 실패하면 음수를 리턴한다.
     */
    fun getRootCommentID(parentID : Long) : Long{
        val parent = mDataset.find { it.mId == parentID } ?: return -1;
        if(parent.mParentID != null){
            return getRootCommentID(parent.mParentID)
        }
        return parent.mId;
    }
}
