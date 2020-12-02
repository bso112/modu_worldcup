package com.manta.worldcup.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Picture(val pictureModel: PictureModel, val mBitmap: Bitmap);

data class PictureModel(
    @SerializedName("id")
    val mId : Long,
    @SerializedName("winCnt")
    val WinCnt: Int,
    @SerializedName("name")
    var mPictureName: String
): Serializable;
