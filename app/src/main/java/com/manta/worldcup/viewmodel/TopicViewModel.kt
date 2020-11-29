package com.manta.worldcup.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.helper.Constants
import com.manta.worldcup.model.Picture
import com.manta.worldcup.model.PictureModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.lang.Exception


class TopicViewModel() : ViewModel() {

    private val repository: Repository = Repository();

    val mTopics: MutableLiveData<Response<List<TopicModel>>> = MutableLiveData();

    fun getAllTopics() {
        viewModelScope.launch {
            val response = repository.getAllTopic()
            mTopics.value = response; //옵저버에게 알림

        }
    }

    fun insertPictureToTopic(pictures : List<Picture>, topicId : Long){
        viewModelScope.launch {
            val bodyParts: ArrayList<MultipartBody.Part> = ArrayList();
            for (picture in pictures) {
                val bitmapData = BitmapHelper.bitmapToByteArray(picture.mBitmap);
                val reqBody = RequestBody.create(MediaType.parse("image/webp"), bitmapData);
                val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
                bodyParts.add(bodyPart);
            }
            try {
                val pictureModels = ArrayList<PictureModel>()
                for (picture in pictures) pictureModels.add(picture.pictureModel);
                repository.insertPictures(topicId, pictureModels, bodyParts);
            } catch (e: Exception) {
                e.message?.let { Log.d(javaClass.toString(), it) };
            }
        }

    }

    suspend fun getTopicImageNames(topicId : Long) = repository.getPicturesName(topicId);

    fun insertTopic(topicModel: TopicModel, pictures: List<Picture>) {
        viewModelScope.launch {

            //비트맵을 byteArray로 bodyPart에 쓰기.
            val bodyParts: ArrayList<MultipartBody.Part> = ArrayList();
            for (picture in pictures) {
                val bitmapData = BitmapHelper.bitmapToByteArray(picture.mBitmap);
                val reqBody = RequestBody.create(MediaType.parse("image/webp"), bitmapData);
                //filename null주면 서버에서 못받음.
                val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
                bodyParts.add(bodyPart);
            }
            try {
                val pictureModels = ArrayList<PictureModel>()
                for (picture in pictures) pictureModels.add(picture.pictureModel);
                repository.insertTopic(topicModel, pictureModels, bodyParts);
            } catch (e: Exception) {
                e.message?.let { Log.d(javaClass.toString(), it) };
            }


        }
    }


}