package com.manta.worldcup.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Picture(val pictureModel : PictureModel, val mBitmap: Bitmap, val mComments : List<Comment>);

data class PictureModel(
    @SerializedName("winCnt")
    val WinCnt : Int,
    @SerializedName("name")
    var mPictureName : String);