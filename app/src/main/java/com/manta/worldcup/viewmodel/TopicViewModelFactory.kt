package com.manta.worldcup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manta.worldcup.repository.Repository

class TopicViewModelFactory() : ViewModelProvider.Factory {
    private val repository: Repository = Repository();

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopicViewModel(repository) as T;
    }


}