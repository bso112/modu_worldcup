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

class MainViewModel(private val application: Application) : ViewModel() {
    private val mRepository: Repository = Repository(application);

    val mPictures: MutableLiveData<List<PictureModel>> = MutableLiveData();
    val mUser: MutableLiveData<User?> = MutableLiveData();


    fun getAllPicture() {
        if(mUser.value == null) return;
        viewModelScope.launch {
            val result = mRepository.getAllPictures(mUser.value!!.mEmail)
            if (result.isSuccessful)
                mPictures.value = result.body();
        }
    }

    fun CheckUserSignIn(onSignIn: (user: User) -> Unit, onSignOut: ((user: User) -> Unit)? = null) {
        AuthSingleton.getInstance(application).CheckUserSignIn({
            mUser.value = it;
            onSignIn(it);
        }, {
            mUser.value = null;
            if (onSignOut != null) onSignOut(it)
        });
    }

}