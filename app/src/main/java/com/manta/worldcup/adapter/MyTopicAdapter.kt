package com.manta.worldcup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.MyTopicOptionDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.TopicJoinUser
import kotlinx.android.synthetic.main.item_topic.view.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

/**
 * item_topic4 을 보여주는 리사이클러뷰 어댑터
 * @author 변성욱
 * @param notifiedTopicId notificationBar에 담긴 pending intent로 전달된 notifiedTopicId
 */
class MyTopicAdapter(private val mNotifiedTopicId: String? = null) : RecyclerView.Adapter<MyTopicAdapter.TopicViewHolder>() {

    private var mDataset: List<TopicJoinUser> = ArrayList();
    private var mOnItemClickListener: OnItemClickListener? = null;
    private var mNotification = MutableList(0) { false }
    private lateinit var mContext: Context;

    //mNotifiedTopicId 에 해당하는 뷰의 애니메이션이 처음 실행되는 상태인가?
    private var mIsFirstAnimation: Boolean = true;

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
        val mNotificationBadge: ImageView = view.iv_notification
        val mOptionBtn: ImageButton = view.btn_more

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    mOnItemClickListener?.onItemClick(mDataset.get(adapterPosition));
                    //노티피케이션이 있으면
                    if (mNotificationBadge.visibility == View.VISIBLE) {
                        //노피티케이션 확인했으니 노피티케이션 삭제.
                        val pref = mContext.applicationContext.getSharedPreferences(Constants.PREF_FILE_NOTIFICATION, Context.MODE_PRIVATE)
                        val topicNotifications = pref.getStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, emptySet());
                        topicNotifications?.remove("${mDataset.get(adapterPosition).mId}")
                        pref.edit().putStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, topicNotifications).apply();
                        mNotificationBadge.visibility = View.INVISIBLE
                    }

                }
            }

            //수정, 삭제 옵션 다이어로그 띄우기
            mOptionBtn.setOnClickListener {
                MyTopicOptionDialog().newInstance(mDataset.get(adapterPosition)).show((mContext as AppCompatActivity).supportFragmentManager, null);
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

            if (mNotification[adapterPosition]) {
                mNotificationBadge.visibility = View.VISIBLE;
            }
            //이거 안해주면 이 뷰홀더의 mNotificationBadge는 VISIBLE이고, 그대로 재활용됨.
            else
                mNotificationBadge.visibility = View.INVISIBLE;

            if (mNotifiedTopicId != null && mNotifiedTopicId == data.mId.toString() && mIsFirstAnimation) {
                view.requestFocus()
                val anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_shake)
                view.startAnimation(anim)
                mIsFirstAnimation = false;
            }


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
        mNotification = MutableList(mDataset.size) { false };
        //알림표시
        val pref = mContext.applicationContext.getSharedPreferences(Constants.PREF_FILE_NOTIFICATION, Context.MODE_PRIVATE)
        val notifiedTopicID = pref.getStringSet(Constants.PREF_NOTIFIED_TOPIC_ID, HashSet());
        if (notifiedTopicID!!.isNotEmpty()) {
            for (i in mDataset.indices) {
                if (notifiedTopicID.contains(mDataset[i].mId.toString())) {
                    mNotification[i] = true;
                    notifyItemChanged(i);
                } else
                    mNotification[i] = false;
            }
        }
    }


    fun setOnItemClickListener(listenr: OnItemClickListener) {
        mOnItemClickListener = listenr;
    }

}