package com.manta.worldcup.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.Topic
import com.manta.worldcup.model.TopicJoinUser
import com.manta.worldcup.model.User
import kotlinx.coroutines.launch

class TopicViewModel(private val application: Application) : ViewModel() {

    private val mRepository: Repository = Repository(application);

    val mDataset: MutableLiveData<List<TopicJoinUser>> = MutableLiveData();

    fun getAllTopic() {
        viewModelScope.launch {
            val res = mRepository.getAllTopicJoinUser();
            if(res.isSuccessful){
                mDataset.value = res.body();
            }
        }
    }

    fun getTopics(userEmail : String){
        viewModelScope.launch{
            val response = mRepository.getTopicJoinUsers(userEmail)
            if(response.isSuccessful){
                mDataset.value = response.body();
            }
        }
    }

    /**
     * @param like true if like, false if dislike
     */
    fun UpdateRecommend(like : Boolean, topicId : Long){
        viewModelScope.launch {
            mRepository.updateRecommned(like, topicId)
        }
    }

    fun increaseView(topicId : Long){
        viewModelScope.launch {
            mRepository.increaseView(topicId);
        }
    }


}