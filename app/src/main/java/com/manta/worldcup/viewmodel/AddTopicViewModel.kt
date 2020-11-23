package com.manta.worldcup.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.model.Topic
import com.manta.worldcup.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class AddTopicViewModel : ViewModel() {

    private val repository: Repository = Repository();

    val mTopics: MutableLiveData<Response<Topic>> = MutableLiveData();

    fun insertTopic(topic : Topic) {
        viewModelScope.launch{
            repository.insertTopic(topic)
        }
    }
}