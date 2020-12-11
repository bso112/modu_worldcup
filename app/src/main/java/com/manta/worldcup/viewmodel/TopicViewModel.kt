package com.manta.worldcup.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.model.Picture
import com.manta.worldcup.model.PictureModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception


class TopicViewModel(private val application: Application) : ViewModel() {

    private val mRepository: Repository = Repository(application);

    val mTopics: MutableLiveData<List<TopicModel>> = MutableLiveData();
    val mPictures: MutableLiveData<List<PictureModel>> = MutableLiveData();

    fun getAllTopics() {
        viewModelScope.launch {
            val response = mRepository.getAllTopic()
            if (response.isSuccessful)
                mTopics.value = response.body(); //옵저버에게 알림

        }
    }

    fun getTopics(userEmail : String){
        viewModelScope.launch{
            val response = mRepository.getAllTopic()
            if(response.isSuccessful){
                mTopics.value = response.body();
            }
        }
    }

    fun getPictures(topicId: Long) {
        viewModelScope.launch {
            val res = mRepository.getPictures(topicId);
            if (res.isSuccessful) {
                mPictures.value = res.body();
            }
        }
    }

    fun insertPictureToTopic(pictures: List<Picture>, topicId: Long) {
        viewModelScope.launch {
            val bodyParts: ArrayList<MultipartBody.Part> = ArrayList();
            for (picture in pictures) {
                val bitmapData = BitmapHelper.bitmapToByteArray(picture.mBitmap);
                val reqBody = RequestBody.create("image/webp".toMediaTypeOrNull(), bitmapData);
                val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
                bodyParts.add(bodyPart);
            }

            val pictureModels = ArrayList<PictureModel>()
            for (picture in pictures) pictureModels.add(picture.pictureModel);
            mRepository.insertPictures(topicId, pictureModels, bodyParts);


        }

    }

    suspend fun getTopicImageNames(topicId: Long) = mRepository.getPicturesName(topicId);

    fun insertTopic(topicModel: TopicModel, pictures: List<Picture>) {
        viewModelScope.launch {

            //비트맵을 byteArray로 bodyPart에 쓰기.
            val bodyParts: ArrayList<MultipartBody.Part> = ArrayList();
            for (picture in pictures) {
                val bitmapData = BitmapHelper.bitmapToByteArray(picture.mBitmap);
                val reqBody = RequestBody.create("image/webp".toMediaTypeOrNull(), bitmapData);
                //filename null주면 서버에서 못받음.
                val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
                bodyParts.add(bodyPart);
            }
            try {
                val pictureModels = ArrayList<PictureModel>()
                for (picture in pictures) pictureModels.add(picture.pictureModel);
                mRepository.insertTopic(topicModel, pictureModels, bodyParts);
            } catch (e: Exception) {
                e.message?.let { Log.d(javaClass.toString(), it) };
            }


        }
    }

    fun addPoint(amount : Int, email : String) {
        viewModelScope.launch {
            mRepository.addPoint(amount, email);
        }
    }

    fun addWinCnt(pictureID : Long){
        viewModelScope.launch {
            mRepository.addwinCnt(pictureID);
        }
    }



}