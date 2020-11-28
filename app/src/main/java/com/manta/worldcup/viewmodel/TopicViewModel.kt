package com.manta.worldcup.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.manta.worldcup.model.TopicModel
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.Constants
import kotlinx.coroutines.launch
import retrofit2.Response


class TopicViewModel() : ViewModel() {

    private val repository: Repository = Repository();

    val mTopics: MutableLiveData<Response<List<TopicModel>>> = MutableLiveData();

    fun getAllTopics() {
        viewModelScope.launch {
            val response = repository.getAllTopic()
            mTopics.value = response; //옵저버에게 알림

        }
    }


}