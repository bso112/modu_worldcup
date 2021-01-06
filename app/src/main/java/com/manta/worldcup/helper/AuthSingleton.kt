package com.manta.worldcup.helper

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.manta.worldcup.activity.AddTopicActivity
import com.manta.worldcup.activity.LoginActivity
import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 인증에 관한 처리를 하는 클래스
 */
class AuthSingleton(application: Application) {

    private val mRepository: Repository = Repository(application);

    companion object {
        private var instance: AuthSingleton? = null
        fun getInstance(application: Application): AuthSingleton {
            return instance ?: synchronized(this) {
                instance = AuthSingleton(application);
                return instance as AuthSingleton;
            }
        }
    }

    /**
     * 유저가 로그인했는지, 안했는지 확인한다.
     * 만약 로그인했으면 onSignIn을, 토큰이 만료됬거나 로그아웃했으면 onSignOut을 호출한다.
     */
    fun CheckUserSignIn(onSignIn: (user: User) -> Unit, onSignOut: ((user: User) -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            //로그인체크
            val result = mRepository.sendAccessToken();
            if (result.isSuccessful) {
                //잘못된 토큰이거나 만료되었을때
                if (result.code() == 202) {
                    withContext(Dispatchers.Main) {
                        result.body()?.let {
                            if (onSignOut != null) onSignOut(it)

                        };
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        result.body()?.let { onSignIn(it) };
                    }
                }
            } else{
                Log.d(Constants.LOG_TAG, result.errorBody().toString());
            }
        }
    }


}