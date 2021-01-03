package com.manta.worldcup.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Comment
import com.manta.worldcup.model.User
import com.skydoves.balloon.ArrowConstraints
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.item_comment.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.min


enum class RECOMMEND { LIKE, DISLIKE, EMPTY }

/**
 * @param mUser : 현재 로그인한 유저
 */
class CommentAdapter(private val mUser: User) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mDataset: List<Comment> = ArrayList();

    //현재 추천버튼을 누른 댓글을 표시하기 위한 정보
    private var mRecommend = emptyArray<RECOMMEND>()
    private var mOnProfileClickListener: OnItemClickListener? = null;
    private var mOnRecommendBtnClickListener: OnRecommendBtnClickListener? = null;
    private var mOnCommentChangeListener: OnCommentChangeListener? = null;
    private lateinit var mContext: Context;


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context;
    }

    interface OnItemClickListener {
        fun onItemClick(comment: Comment);
    }

    interface OnRecommendBtnClickListener {
        fun onRecommendChanged(commentID: Long, good: Int, bad: Int)
    }

    interface OnCommentChangeListener {
        fun onMoreButtonClick()
        fun onCommentUpdate(comment: Comment)
        fun onCommentDelete(comment: Comment)
    }


    inner class CommentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val mNickname = view.tv_user_nickname;
        val mProfile = view.iv_profile;
        val mContent = view.tv_content;
        val mDate = view.tv_date;
        val mLikeBtn = view.btn_like;
        val mDislikeBtn = view.btn_dislike;
        val mtvLike = view.tv_like;
        val mtvDislike = view.tv_dislike;
        val mtvReplyMark = view.tv_reply_mark;
        val mMoreBtn = view.btn_more;
        val mModifyBtn = view.btn_modify

        init {

            mNickname.setOnClickListener {
                mOnProfileClickListener?.onItemClick(mDataset[adapterPosition]);
            }
            mProfile.setOnClickListener {
                mOnProfileClickListener?.onItemClick(mDataset[adapterPosition]);
            }

            //추천, 비추천
            mLikeBtn.setOnClickListener {
                if (mRecommend[adapterPosition] == RECOMMEND.LIKE)
                    mRecommend[adapterPosition] = RECOMMEND.EMPTY
                else {
                    mRecommend[adapterPosition] = RECOMMEND.LIKE
                    val comment = mDataset[adapterPosition]
                    mOnRecommendBtnClickListener?.onRecommendChanged(comment.mId, comment.mGood + 1, comment.mBad)
                }

                notifyItemChanged(adapterPosition)
            }
            mDislikeBtn.setOnClickListener {
                if (mRecommend[adapterPosition] == RECOMMEND.DISLIKE)
                    mRecommend[adapterPosition] = RECOMMEND.EMPTY
                else {
                    mRecommend[adapterPosition] = RECOMMEND.DISLIKE
                    val comment = mDataset[adapterPosition]
                    mOnRecommendBtnClickListener?.onRecommendChanged(comment.mId, comment.mGood, comment.mBad + 1)
                }

                notifyItemChanged(adapterPosition)
            }

            mMoreBtn.setOnClickListener {

                mOnCommentChangeListener?.onMoreButtonClick()

                val typedValue = TypedValue();
                mContext.theme.resolveAttribute(R.attr.colorSurface, typedValue, true);
                val colorSurface = ContextCompat.getColor(mContext, typedValue.resourceId)
                var layoutID =
                    if (mDataset[adapterPosition].mWriterEmail == mUser.mEmail)
                        R.layout.dialog_mycomment_option
                    else
                        R.layout.dialog_comment_option


                val balloon = Balloon.Builder(mContext)
                    .setArrowSize(10)
                    .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                    .setArrowPosition(0.5f)
                    .setLayout(layoutID)
                    .setCornerRadius(4f)
                    .setBackgroundColor(colorSurface)
                    .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                    .build()

                balloon.getContentView().findViewById<TextView>(R.id.btn_modify).setOnClickListener {
                    //글 수정 가능하게 UI 갱신
                    mContent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.black))
                    mContent.isEnabled = true;
                    mModifyBtn.visibility = View.VISIBLE
                    balloon.dismiss()
                }

                balloon.getContentView().findViewById<TextView>(R.id.btn_delete).setOnClickListener {
                    mOnCommentChangeListener?.onCommentDelete(mDataset[adapterPosition])
                    balloon.dismiss()
                }

                balloon.show(mMoreBtn)
            }

            mModifyBtn.setOnClickListener {
                //글 수정
                val newComment = mDataset[adapterPosition];
                newComment.mContents = mContent.text.toString()
                mOnCommentChangeListener?.onCommentUpdate(newComment);
                mContent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.transparent))
                mContent.isEnabled = false;
                mModifyBtn.visibility = View.GONE

                notifyItemChanged(adapterPosition)
            }
        }


        fun setComment(comment: Comment) {
            mNickname.text = comment.mWriter;
            mContent.setText(comment.mContents);
            mDate.text = comment.mDate
            mtvLike.text = comment.mGood.toString();
            mtvDislike.text = comment.mBad.toString();

            //프로필 이미지 셋팅
            val profileUrl = Constants.BASE_URL + "user/profile_image/" + comment.mWriterEmail;
            Constants.GlideWithHeader(profileUrl, mProfile, mProfile, view.context)


            val locale = view.context.applicationContext.resources.configuration.locale;
            val transFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", locale);
            val date = transFormat.parse(comment.mDate)
            date?.let {
                val timePassed = Constants.getTimePassedFromNow(view.context.resources, it)
                mDate.text = timePassed
            }


            //추천, 비추천 표시
            val primaryColorState = ColorStateList.valueOf(Constants.resolveAttribute(view.context, R.attr.colorPrimary))
            val disabledColorState = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.disabled))
            mLikeBtn.backgroundTintList = disabledColorState
            mDislikeBtn.backgroundTintList = disabledColorState
            when (mRecommend[adapterPosition]) {
                RECOMMEND.LIKE -> {
                    mLikeBtn.backgroundTintList = primaryColorState;
                    mtvLike.text = (comment.mGood + 1).toString();
                }
                RECOMMEND.DISLIKE -> {
                    mDislikeBtn.backgroundTintList = primaryColorState;
                    mtvDislike.text = (comment.mBad + 1).toString();


                }
            }


            //대댓글인지 표시
            if (comment.mParentID != null)
                mtvReplyMark.visibility = View.VISIBLE
            else
                mtvReplyMark.visibility = View.GONE


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

    //다시 재정의..


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false);
        return CommentViewHolder(view);

    }


    override fun getItemCount(): Int {
        return mDataset.size;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CommentViewHolder).setComment(mDataset[position]);
    }

    fun setComments(comments: List<Comment>) {
        val nomalized = nomalizeComments(comments)
        val result = DiffUtil.calculateDiff(CommentDiffUtilCallback(mDataset, nomalized))
        mDataset = nomalized;
        mRecommend = Array(mDataset.size) { RECOMMEND.EMPTY }

        result.dispatchUpdatesTo(this)
    }

    //베스트댓글을 맨위로 올리고 대댓글 위치조정
    //대댓글이 베스트가 되는건 막는다.
    private fun nomalizeComments(comments: List<Comment>): List<Comment> {
        if (comments.isEmpty())
            return comments;

        val result = LinkedList<Comment>();

        for (comment in comments) {
            if (comment.mParentID == null)
                result.add(comment)
        }

        if (result.isEmpty())
            return comments;

        var maxRecommend = 0
        var bestCommentIndex = 0
        for (index in 0 until result.size) {
            if (result[index].mParentID != null) continue;
            if (maxRecommend < result[index].mGood) {
                maxRecommend = result[index].mGood
                bestCommentIndex = index;
            }
        }
        //swap
        result[0] = result[bestCommentIndex].also { result[bestCommentIndex] = result[0] }
        


        //대댓글 위치조정
        //parent는 result에, 자식은 comment 에 있다.
        for (index in comments.indices) {
            if (comments[index].mParentID != null) {
                val parentIndex = result.indexOfFirst { it.mId == comments[index].mParentID }
                if (parentIndex >= 0) {
                    result.add(min(parentIndex + 1, result.size), comments[index])
                } else {
                    val a = 4;
                }
            }
        }

        return result;

    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnProfileClickListener = listener;
    }

    fun setOnRecommendBtnClickListener(listener: OnRecommendBtnClickListener) {
        mOnRecommendBtnClickListener = listener
    }

    fun setOnCommentChangeListener(listener: OnCommentChangeListener) {
        mOnCommentChangeListener = listener
    }

    /**
     * 재귀적으로 root 코멘트 (부모가 없는 코멘트)를 찾아준다.
     * 실패하면 음수를 리턴한다.
     */
    fun getRootCommentID(parentID: Long): Long {
        val parent = mDataset.find { it.mId == parentID } ?: return -1;
        if (parent.mParentID != null) {
            return getRootCommentID(parent.mParentID)
        }
        return parent.mId;
    }


}
