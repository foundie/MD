package com.foundie.id.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.DataPostItem

class UserPostViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val userPostStatus: LiveData<String> = mainRepository.message

    val isLoadingUserPost: LiveData<Boolean> = mainRepository.isLoading

    val userPost: LiveData<List<DataPostItem>> = mainRepository.postuser

    var isErrorPost: Boolean = false

    init {
        userPostStatus.observeForever { status ->
            isErrorPost = status != ""
        }
    }

    fun getPostUser(token:String) {
        mainRepository.getPostUser(token)
    }
}
