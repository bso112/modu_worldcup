package com.manta.worldcup.model

import android.graphics.Bitmap

data class Picture(val mComments : List<Comment>, var mDescription : String,
                   val mBitmap : Bitmap, val WinCnt : Int, var mPictureName : String);