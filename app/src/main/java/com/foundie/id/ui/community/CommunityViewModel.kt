package com.foundie.id.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.AddPostUserResponse
import com.foundie.id.data.local.response.DataPostItem
import com.foundie.id.data.local.response.GroupsDataItem
import com.foundie.id.data.local.response.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CommunityViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val userPostStatus: LiveData<String> = mainRepository.message
    val listGroupStatus: LiveData<String> = mainRepository.message
    val addPostStatus: LiveData<String> = mainRepository.addpostStatus
    val createCommunityStatus: LiveData<String> = mainRepository.createCommunityStatus

    val isLoadingUserPost: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingListGroup: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingAddPost: LiveData<Boolean> = mainRepository.isLoadingAddPost
    val isLoadingCommunity: LiveData<Boolean> = mainRepository.isLoadingCommunity

    val userPost: LiveData<List<DataPostItem>> = mainRepository.postuser
    val listGroup: LiveData<List<GroupsDataItem>> = mainRepository.listGroup

    var isErrorPost: Boolean = false
    var isErrorAddPost: Boolean = false
    var isErrorCommunity: Boolean = false
    var isErrorListGroup: Boolean = false

    init {
        userPostStatus.observeForever { status ->
            isErrorPost = status != ""
        }
        addPostStatus.observeForever { status ->
            isErrorAddPost = status != ""
        }
        createCommunityStatus.observeForever { status ->
            isErrorCommunity = status != ""
        }
        listGroupStatus.observeForever { status ->
            isErrorListGroup= status != ""
        }
    }

    fun getPostUser(token:String) {
        mainRepository.getPostUser(token)
    }

    fun getListGroup(token:String) {
        mainRepository.getGroup(token)
    }

    fun createCommunity(
        token: String,
        coverImage: MultipartBody.Part,
        profileImage: MultipartBody.Part,
        title: RequestBody,
        topics:RequestBody,
        description: RequestBody
    ) {
        mainRepository.createCommunity(token, coverImage, profileImage , title, topics, description)
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
