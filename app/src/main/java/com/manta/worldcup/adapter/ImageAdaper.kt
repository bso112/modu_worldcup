package com.manta.worldcup.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import kotlinx.android.synthetic.main.item_picture.view.*

class ImageAdaper : RecyclerView.Adapter<ImageAdaper.ImageViewHolder>(){

    var mDataset : ArrayList<Bitmap> = ArrayList();

    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val picture : ImageView = view.iv_picture;
        val cancelBtn : ImageButton  = view.btn_cancel;

        init {
            cancelBtn.setOnClickListener {
                removeImage(adapterPosition);
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
        holder.picture.setImageBitmap(mDataset[position]);
    }

    fun removeImage(position : Int){
        if(mDataset.size <= position)
            return;
        mDataset.removeAt(position);
        notifyItemRemoved(position)
    }

    /**
     * by변성욱
     * 토픽에 들어갈 비트맵들의 미리보기를 이 어댑터에 제공한다.
     * notifyDataSetChanged()를 부르므로, 자주부르지 않는 것이 좋다.
     */
    fun setBitmaps(bitmaps : ArrayList<Bitmap>){
        mDataset = bitmaps;
        notifyDataSetChanged();
    }



}