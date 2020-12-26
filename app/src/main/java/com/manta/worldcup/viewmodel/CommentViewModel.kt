package com.manta.worldcup.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.Comment
import kotlinx.coroutines.launch

class CommentViewModel(application: Context) : ViewModel() {

    private val repository: Repository = Repository(application);
    val mComments: MutableLiveData<List<Comment>> = MutableLiveData();

    fun getTopicComments(topicId: Long){
        viewModelScope.launch {
            val result = repository.getTopicComment(topicId)
            if(result.isSuccessful){
                mComments.value = result.body();
            }
            else
                Log.d(javaClass.toString(), result.errorBody().toString());
        }
    }

    fun getPictureComment(pictureId : Long){
        viewModelScope.launch {
            val result = repository.getPictureComment(pictureId)
            if(result.isSuccessful){
                mComments.value = result.body();
            }
            else
                Log.d(javaClass.toString(), result.errorBody().toString());
        }
    }

    fun insertTopicComment(comment : Comment){
        viewModelScope.launch {
            val result = repository.insertTopicComment(comment);
            if(result.isSuccessful){
                //현재 댓글을 갱신한다.  commentID를 갱신해야하므로 새로운 코멘트 리스트를 받아야한다.
                mComments.value = result.body()
            } else Log.d(javaClass.toString(), result.errorBody().toString());

        }
    }

    fun insertPictureComment(comment : Comment){
        viewModelScope.launch {
            val result = repository.insertPictureComment(comment);
            if(result.isSuccessful){
                //현재 댓글을 갱신한다. commentID를 갱신해야하므로 새로운 코멘트 리스트를 받아야한다.
                mComments.value = result.body()

            } else Log.d(javaClass.toString(), result.errorBody().toString());

        }
    }

    fun updateRecommend(commendID : Long, isLike : Boolean){
        viewModelScope.launch {
            repository.updateRecommend(commendID, isLike);
        }
    }

    fun deleteComment(comment : Comment){
        viewModelScope.launch {
            val response = repository.deleteComment(comment)
            if(response.isSuccessful){
                mComments.value = response.body()
            }
        }
    }


    fun updateComment(comment : Comment){
        viewModelScope.launch {
            val response = repository.updateComment(comment)
            if(response.isSuccessful){
                mComments.value = response.body()
            }
        }
    }

}