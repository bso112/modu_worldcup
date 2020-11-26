package com.manta.worldcup.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.model.Topic
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.BitmapHelper
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class AddTopicViewModel : ViewModel() {

    private val repository: Repository = Repository();

    val mTopics: MutableLiveData<Response<Topic>> = MutableLiveData();

    fun insertTopic(topic: Topic) {
        viewModelScope.launch {

            val bodyParts : ArrayList<MultipartBody.Part> = ArrayList();
            for(picture in topic.mPictures){
                val bitmapData = BitmapHelper.bitmapToByteArray(picture.mBitmap);
                val reqBody = RequestBody.create(MediaType.parse("image/webp"), bitmapData);
                //filename null주면 서버에서 못받음.
                val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
                bodyParts.add(bodyPart);
            }
             repository.insertTopic(bodyParts)




        }
    }
}