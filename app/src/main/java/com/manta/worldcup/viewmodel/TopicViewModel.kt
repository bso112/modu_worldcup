package com.manta.worldcup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.model.Topic
import com.manta.worldcup.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class TopicViewModel(private val repository: Repository)  : ViewModel(){

    val mTopics : MutableLiveData<Response<List<Topic>>> = MutableLiveData();

    fun getAllTopics() {
        viewModelScope.launch{
            val response = repository.getAllTopic();
            mTopics.value = response;
        }
    }

}