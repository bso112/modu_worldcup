package com.manta.worldcup.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.Comment
import kotlinx.android.synthetic.main.frag_topic.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import retrofit2.Response

class CommentViewModel(application: Context) : ViewModel() {

    private val repository: Repository = Repository(application);
    val mComments: MutableLiveData<ArrayList<Comment>> = MutableLiveData();

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

    fun addRecommend(commendID : Long){
        viewModelScope.launch {
            repository.addRecommend(commendID);
        }
    }

}