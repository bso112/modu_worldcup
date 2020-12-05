package com.manta.worldcup.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.Comment
import kotlinx.coroutines.launch

class CommentViewModel(application: Application) : ViewModel() {

    private val repository: Repository = Repository(application);
    val mComments: MutableLiveData<ArrayList<Comment>> = MutableLiveData();

    fun getTopicComments(topicId: Long) {
        viewModelScope.launch {
            val result = repository.getComments(topicId)
            if(result.isSuccessful){
                mComments.value = result.body();
            }
            else
                Log.d(javaClass.toString(), result.errorBody().toString());
        }
    }

    fun insertComment(comment : Comment){
        viewModelScope.launch {
            val result = repository.insertComment(comment);
            if(result.isSuccessful){
                mComments.value?.add(comment);
            } else Log.d(javaClass.toString(), result.errorBody().toString());

        }
    }


}