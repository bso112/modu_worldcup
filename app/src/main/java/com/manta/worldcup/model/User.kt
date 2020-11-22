package com.manta.worldcup.model

data class User (val mUserPicture : List<Picture>, val mNickname : String,
                 val mTier : Int, val mNotification : List<String>, val mCurrPoint : Int);
