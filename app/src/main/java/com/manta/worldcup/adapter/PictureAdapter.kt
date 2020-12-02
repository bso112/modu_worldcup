package com.manta.worldcup.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.activity.fragment.dialog.PictureDescriptionDialog
import com.manta.worldcup.model.Picture
import com.manta.worldcup.model.PictureModel
import kotlinx.android.synthetic.main.item_picture.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class PictureAdapter(private val fragmentManager : FragmentManager) : RecyclerView.Adapter<PictureAdapter.ImageViewHolder>() {

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
        val mCheckedView : ImageView = view.iv_checked;

        init {
            //엑스클릭하면 항목지우기
            mCancelBtn.setOnClickListener {
                //removeImage(adapterPosition); 하고나면 adapterPosition 바뀌어버려서 먼저해야함.
                if(adapterPosition < mIsPictureNamed.size)
                    mIsPictureNamed.removeAt(adapterPosition);
                removeImage(adapterPosition);
            }

            //사진클릭하면 사진이름 정하는 다이어로그 띄우기
            mPictureView.setOnClickListener {
                //array copy
                val pictureNames = ArrayList(mPictureNames);
                //현재 추가된 이름들도 중복확인리스트에 포함한다.
                for (picture in mDataset) pictureNames.add(picture.pictureModel.mPictureName);
                PictureDescriptionDialog().newInstance(pictureNames, object : PictureDescriptionDialog.OnSubmitListener {
                    //결과받아 표시하기
                    override fun onSubmit(pictureName: String) {
                        mDataset[adapterPosition].pictureModel.mPictureName = pictureName;
                        mCheckedView.visibility = View.VISIBLE;
                        mIsPictureNamed[adapterPosition] = true;
                    }
                }).show(fragmentManager, null);


            }
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
        holder.mPictureView.setImageBitmap(mDataset[position].mBitmap);
    }

    fun removeImage(position: Int) {
        if (mDataset.size <= position)
            return;
        mDataset.removeAt(position);
        notifyItemRemoved(position)
    }


    fun addBitmap(bitmap: Bitmap) {
        mDataset.add(Picture(PictureModel(0, 0, ""), bitmap));
        mIsPictureNamed.add(false);
        notifyItemInserted(mDataset.size - 1);
    }

    fun getPictures() = ArrayList(mDataset);

    /**
     * by 변성욱
     * 중복확인에 사용할 사진이름들을 설정한다.
     */
    fun setPictureNames(pictureNames : HashSet<String>){
        mPictureNames = pictureNames;
    }

    /**
     * by 변성욱
     * 모든 사진의 이름이 지어졌는가?
     */
    fun isPicturesReadyToSubmit() : Boolean{
        for(isNamed in mIsPictureNamed){
            if(!isNamed) return false;
        }
        return true;
    }

}