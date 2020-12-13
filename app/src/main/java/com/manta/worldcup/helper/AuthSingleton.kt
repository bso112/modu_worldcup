package com.manta.worldcup.helper

import android.app.Application
import android.content.Context
import android.content.Intent
import com.manta.worldcup.activity.AddTopicActivity
import com.manta.worldcup.activity.LoginActivity
import com.manta.worldcup.api.RetrofitInstance
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            }
        }
    }


}