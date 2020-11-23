package com.manta.worldcup.viewmodel

import androidx.lifecycle.*
import com.manta.worldcup.model.Topic
import com.manta.worldcup.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response


class TopicViewModel()  : ViewModel(){

    private val repository: Repository = Repository();

    val mTopics : MutableLiveData<Response<List<Topic>>> = MutableLiveData();

    fun getAllTopics() {
        viewModelScope.launch{
            val response = repository.getAllTopic();
            mTopics.value = response;
        }
    }

}