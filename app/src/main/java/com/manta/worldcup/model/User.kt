package com.manta.worldcup.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (
    @SerializedName("email")
    val mEmail : String,
    @SerializedName("nickname")
    val mNickname : String,
    @SerializedName("tier")
    val mTier : Int,
    @SerializedName("point")
    val mCurrPoint : Int) : Serializable;
