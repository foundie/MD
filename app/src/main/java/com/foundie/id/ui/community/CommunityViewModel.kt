package com.foundie.id.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.AddPostUserResponse
import com.foundie.id.data.local.response.DataPostItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CommunityViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val userPostStatus: LiveData<String> = mainRepository.message
    val addPostStatus: LiveData<String> = mainRepository.addpostStatus

    val isLoadingUserPost: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingAddPost: LiveData<Boolean> = mainRepository.isLoadingAddPost

    val userPost: LiveData<List<DataPostItem>> = mainRepository.postuser

    var isErrorPost: Boolean = false
    var isErrorAddPost: Boolean = false

    init {
        userPostStatus.observeForever { status ->
            isErrorPost = status != ""
        }
        addPostStatus.observeForever { status ->
            isErrorAddPost = status != ""
        }
    }

    fun getPostUser(token:String) {
        mainRepository.getPostUser(token)
    }

    fun addPostUser(
        token: String,
        postImage: MultipartBody.Part,
        title: RequestBody,
        desc: RequestBody,
    ) {
        mainRepository.addPostUser(token, postImage, title, desc)
    }
}
