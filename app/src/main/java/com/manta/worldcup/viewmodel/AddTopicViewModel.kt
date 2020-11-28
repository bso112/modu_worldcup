package com.manta.worldcup.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.model.PictureModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.lang.Exception

class AddTopicViewModel : ViewModel() {

    private val repository: Repository = Repository();

    val mTopics: MutableLiveData<Response<TopicModel>> = MutableLiveData();

    fun insertTopic(topicModel: TopicModel, pictures : List<PictureModel>) {
        viewModelScope.launch {

            val bodyParts : ArrayList<MultipartBody.Part> = ArrayList();
            for(picture in pictures){
                val bitmapData = BitmapHelper.bitmapToByteArray(picture.mBitmap);
                val reqBody = RequestBody.create(MediaType.parse("image/webp"), bitmapData);
                //filename null주면 서버에서 못받음.
                val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
                bodyParts.add(bodyPart);
            }
            try {
             repository.insertTopic(topicModel, bodyParts);
            }catch (e : Exception){
                e.message?.let { Log.d(javaClass.toString(), it) };
            }



        }
    }
}