package com.manta.worldcup.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureDescriptionDialog
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Picture
import com.manta.worldcup.model.PictureModel
import kotlinx.android.synthetic.main.item_picture.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * 토픽에 사진을 추가시 추가된 사진을 보여주는 어댑터이다.
 */
class TopicPictureAdapter(private val fragmentManager: FragmentManager) : RecyclerView.Adapter<TopicPictureAdapter.ImageViewHolder>() {

    private val mDataset: ArrayList<Picture> = ArrayList();

    /**
     * by 변성욱
     * 각 사진들이 이름이 지어졌는가
     */
    private val mIsPictureNamed = LinkedList<Boolean>();
    private var mPictureNames = HashSet<String>();

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mPictureView: ImageView = view.iv_picture;
        val mCancelBtn: ImageButton = view.btn_cancel;

        //이미지 제목이 입력되었다는 표시
        val mCheckedView: ImageView = view.iv_checked;

        init {
            //엑스클릭하면 항목지우기
            mCancelBtn.setOnClickListener {
                removeImage(adapterPosition);
            }

            //사진클릭하면 사진이름 정하는 다이어로그 띄우기
            mPictureView.setOnClickListener {
                //array copy
                val pictureNames = ArrayList(mPictureNames);
                //현재 추가된 이름들도 중복확인리스트에 포함한다.
                for (picture in mDataset) pictureNames.add(picture.pictureModel.mPictureName);
                PictureDescriptionDialog().newInstance(
                    pictureNames,
                    mDataset[adapterPosition].pictureModel.mPictureName,
                    object : PictureDescriptionDialog.OnSubmitListener {
                        //이름이 정해진 사진을 표시하기
                        override fun onSubmit(pictureName: String) {
                            mDataset[adapterPosition].pictureModel.mPictureName = pictureName;
                            mCheckedView.visibility = View.VISIBLE;
                            mIsPictureNamed[adapterPosition] = true;
                        }
                    }).show(fragmentManager, null);
            }
        }

        fun setPicture(picture : Picture) {
            //이미 이름이 있는 사진이면 체크표시
            if (picture.pictureModel.mPictureName != "")
                mCheckedView.visibility = View.VISIBLE;
            else
                mCheckedView.visibility = View.INVISIBLE

            //서버로부터 이미지 요청
            if (picture.mBitmap == null) {
                val url = Constants.BASE_URL + "image/get/${picture.pictureModel.mId}/";
                Constants.GlideWithHeader(url, mPictureView, mPictureView, mPictureView.context)
            }
            //비트맵 셋팅
            else
                mPictureView.setImageBitmap(picture.mBitmap);
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false);
        return ImageViewHolder(view);
    }

    override fun getItemCount(): Int {
        return mDataset.size

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setPicture(mDataset[position])
    }

    fun removeImage(position: Int) {
        if (mDataset.size <= position)
            return;
        mDataset.removeAt(position);
        mIsPictureNamed.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     *  갤러리에서 사진을 추가한 경우 (토픽 추가)
     *  @param bitmap 사진에 넣을 비트맵
     *  @param ownerEmail 사진을 가진 유저의 이메일
     */
    fun addPicture(bitmap: Bitmap, ownerEmail: String) {
        mDataset.add(Picture(PictureModel(0, 0, "", "", 0, ownerEmail), bitmap));
        mIsPictureNamed.add(false);
        notifyItemInserted(mDataset.size - 1);
    }

    /**
     *  기존에 토픽에 등록된 서버에서 사진을 받아오는 경우(토픽 업데이트)
     */
    fun setPictures(pictureModels: List<PictureModel>) {
        mDataset.clear();
        mIsPictureNamed.clear()
        for (pictureModel in pictureModels) {
            mDataset.add(Picture(pictureModel, null));
            mIsPictureNamed.add(true)
            notifyItemInserted(mDataset.size - 1);
        }
    }

    fun getPictures() = ArrayList(mDataset);

    /**
     * by 변성욱
     * 기존 토픽에 사진을 추가할경우, 중복확인에 사용할 사진이름들을 설정한다.
     */
    fun setPictureNames(pictureNames: HashSet<String>) {
        mPictureNames = pictureNames;
    }

    /**
     * by 변성욱
     * 모든 사진의 이름이 지어졌는가?
     */
    fun isPicturesReadyToSubmit(): Boolean {
        for (isNamed in mIsPictureNamed) {
            if (!isNamed) return false;
        }
        return true;
    }

    fun getPictureSize() = mDataset.size;

}