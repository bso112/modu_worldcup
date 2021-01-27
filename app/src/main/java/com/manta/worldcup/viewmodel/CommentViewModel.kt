package com.manta.worldcup.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.Comment
import kotlinx.coroutines.launch

class CommentViewModel(application: Context) : ViewModel() {

    private val repository: Repository = Repository(application);
    val mComments: MutableLiveData<List<Comment>> = MutableLiveData();

    companion object {
        fun provideViewModel(owner: ViewModelStoreOwner, application: Application) =
            ViewModelProvider(owner, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return CommentViewModel(application) as T;
                }
            }).get(CommentViewModel::class.java);
    }



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

    fun updateTopicCommentRecommend(commendID : Long, good : Int, bad : Int){
        viewModelScope.launch {
            repository.updateTopicCommentRecommend(commendID, good, bad);
        }
    }

    fun deleteTopicComment(comment : Comment){
        viewModelScope.launch {
            val response = repository.deleteTopicComment(comment)
            if(response.isSuccessful){
                mComments.value = response.body()
            }
        }
    }


    fun updateTopicComment(comment : Comment){
        viewModelScope.launch {
            val response = repository.updateTopicComment(comment)
            if(response.isSuccessful){
                mComments.value = response.body()
            }
        }
    }



    fun updatePictureRecommend(commendID : Long, good : Int, bad : Int){
        viewModelScope.launch {
            repository.updatePictureCommentRecommend(commendID, good, bad);
        }
    }

    fun deletePictureComment(comment : Comment){
        viewModelScope.launch {
            val response = repository.deletePictureComment(comment)
            if(response.isSuccessful){
                mComments.value = response.body()
            }
        }
    }


    fun updatePictureComment(comment : Comment){
        viewModelScope.launch {
            val response = repository.updatePictureComment(comment)
            if(response.isSuccessful){
                mComments.value = response.body()
            }
        }
    }

    fun reportPictureComment(commentID : Long){
        viewModelScope.launch {
            repository.reportPictureComment(commentID)
        }
    }

    fun reportTopicComment(commentID : Long){
        viewModelScope.launch {
            repository.reportTopicComment(commentID)
        }
    }

}