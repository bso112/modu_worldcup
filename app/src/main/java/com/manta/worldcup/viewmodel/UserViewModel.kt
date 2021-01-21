package com.manta.worldcup.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.AuthSingleton
import com.manta.worldcup.helper.BitmapHelper
import com.manta.worldcup.model.PictureModel
import com.manta.worldcup.model.User
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * 유저에 대한 데이터를 처리하는 뷰모델
 * ->리팩토링 : viewModel은 이렇게 데이터타입 단위로말고, view 단위로 만들어야할듯
 * (view 단위지, 꼭 activity단위여야할 필요는 없다)
 * 원래 viewModel은 동일하거나 거의 비슷한 view가 아니면 재사용하지 않는듯.
 */
class UserViewModel(private val application: Application) : ViewModel() {
    private val mRepository: Repository = Repository(application);

    val mPictures: MutableLiveData<List<PictureModel>> = MutableLiveData();
    val mUser: MutableLiveData<User?> = MutableLiveData();


    fun getUserPictures() {
        if (mUser.value == null) return;
        viewModelScope.launch {
            val result = mRepository.getAllPictures(mUser.value!!.mEmail)
            if (result.isSuccessful)
                mPictures.value = result.body();
        }
    }

    fun removeProfileImage(onRemove : (()->Unit)? = null){
        viewModelScope.launch {
            val result = mRepository.removeProfileImage(mUser.value!!.mEmail, mUser.value!!.mProfileImgName)
            if(result.isSuccessful){
                if (onRemove != null) {
                    onRemove()
                };
            }
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
            }
            onSignIn(it);
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

    fun updateUser(user: User) {
        viewModelScope.launch {
            mRepository.updateUser(user)
        }
    }

    fun getUser(userEmail: String) {
        viewModelScope.launch {
            val result = mRepository.getUser(userEmail);
            if (result.isSuccessful) {
                mUser.value = result.body()
            }
        }
    }

    fun uploadProfileImage(image: Bitmap, onSucess: (() -> Unit)? = null) {
        mUser.value ?: return;
        viewModelScope.launch {
            val bitmapData = BitmapHelper.bitmapToByteArray(image);
            val reqBody = RequestBody.create("image/webp".toMediaTypeOrNull(), bitmapData);
            val bodyPart = MultipartBody.Part.createFormData("image", "tmp", reqBody);
            val response = mRepository.uploadProfileImage(mUser.value!!.mEmail, mUser.value!!.mProfileImgName, bodyPart)
            if (response.isSuccessful)
                if (onSucess != null) {
                    onSucess()
                };
        }
    }

}