package com.manta.worldcup.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.manta.worldcup.R
import com.manta.worldcup.model.Picture
import kotlinx.android.synthetic.main.item_picture.view.*

class ImageAdaper : RecyclerView.Adapter<ImageAdaper.ImageViewHolder>(){

    private val mDataset : ArrayList<Picture> = ArrayList();

    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val picture : ImageView = view.iv_picture;
        val cancelBtn : ImageButton  = view.btn_cancel;
        val title : EditText = view.et_title;
        val description : EditText = view.et_description;

        init {
            cancelBtn.setOnClickListener {
                removeImage(adapterPosition);
            }
            title.addTextChangedListener{
                mDataset[adapterPosition].mPictureName = it.toString();
            }
            description.addTextChangedListener {
                mDataset[adapterPosition].mDescription = it.toString();
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
        holder.picture.setImageBitmap(mDataset[position].mBitmap);
    }

    fun removeImage(position : Int){
        if(mDataset.size <= position)
            return;
        mDataset.removeAt(position);
        notifyItemRemoved(position)
    }


    fun addBitmap(bitmap : Bitmap){
        mDataset.add(Picture(emptyList(), "", bitmap, 0, ""));
        notifyItemInserted(mDataset.size - 1);
    }

    fun getPictures() = ArrayList(mDataset);





}