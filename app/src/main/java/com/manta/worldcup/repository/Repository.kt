package com.manta.worldcup.repository

import android.app.Application
import com.manta.worldcup.dataclass.User
import com.manta.worldcup.repository.Dao.PictureDao
import com.manta.worldcup.repository.Dao.UserDao

class Repository(application: Application) {

    private val mUserDao : UserDao
    private val mPictureDao : PictureDao

    init {
        val database = Database.getInstance(application);
        mUserDao = database.userDao();
        mPictureDao = database.pictureDao();
    }

    fun insertUser(user : User){
        mUserDao.insert(user);
    }

    fun deleteUser(user : User){
        mUserDao.delete(user);
    }

    fun updateUser(user : User){
        mUserDao.update(user);
    }







}