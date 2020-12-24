package com.manta.worldcup.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.AuthSingleton
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.User
import kotlinx.coroutines.launch

class UserViewModel(private val application: Application) : ViewModel() {
    private val mRepository: Repository = Repository(application);

    val mPictures: MutableLiveData<List<PictureModel>> = MutableLiveData();
    val mUser: MutableLiveData<User?> = MutableLiveData();


    fun getAllPicture() {
        if (mUser.value == null) return;
        viewModelScope.launch {
            val result = mRepository.getAllPictures(mUser.value!!.mEmail)
            if (result.isSuccessful)
                mPictures.value = result.body();
        }
    }

    /**
     * 유저가 로그인했는지 확인한다.
     * @param onSignIn 로그인한 상태이고, 로그인 정보가 달라졌을때만 불린다.
     */
    fun CheckUserSignIn(onSignIn: (user: User) -> Unit, onSignOut: ((user: User) -> Unit)? = null) {
        AuthSingleton.getInstance(application).CheckUserSignIn({
            if (mUser.value != it) {
                mUser.value = it;
                onSignIn(it);
            }
        }, {
            mUser.value = null;
            if (onSignOut != null) onSignOut(it)
        });
    }

    fun registerFirebaseToken(userEmail: String, token: String) {
        viewModelScope.launch {
            mRepository.registerFirebaseToken(userEmail, token);
        }
    }

    fun updateUserNickname(nickname: String, onSucess: () -> Unit) {
        viewModelScope.launch {
            mUser.value?.mEmail?.let {
                val response = mRepository.updateUserNickname(nickname, it)
                if (response.isSuccessful)
                    onSucess();
            };
        }
    }

}