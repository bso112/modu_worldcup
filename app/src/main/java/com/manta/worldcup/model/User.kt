package com.manta.worldcup.model

data class User (val mUserPictureModel : List<PictureModel>, val mNickname : String,
                 val mTier : Int, val mNotification : List<String>, val mCurrPoint : Int);
