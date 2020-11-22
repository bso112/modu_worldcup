package com.manta.worldcup.dataclass

data class Topic(val id : Long, val title : String, val mComments : List<Comment>, val mPictures : List<Picture>, val mManager : User){

    /**
     * 토픽에 해당 이름과 동일한 사진이 없는가?
     * @param pictureName 검사할 이름
     */
    fun isPictureUnique(pictureName : String) : Boolean{
        return true;
    }
}