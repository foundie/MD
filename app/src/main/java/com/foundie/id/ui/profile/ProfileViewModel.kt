package com.foundie.id.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val profileStatus: LiveData<String> = mainRepository.message
    val editprofileStatus: LiveData<String> = mainRepository.editBiodataStatus

    val isLoadingProfile: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingeditBiodata: LiveData<Boolean> = mainRepository.isLoadingeditBiodata

    var isErrorProfile: Boolean = false
    var isErroreditBiodata: Boolean = false

    init {
        profileStatus.observeForever { status ->
            isErrorProfile = status != ""
        }
    }

    val biodata:  LiveData<User> = mainRepository.biodata

    fun getBiodata(token: String) {
        mainRepository.getBiodata(token)
    }

    fun editBiodata(
        token: String,
        coverImage: MultipartBody.Part,
        profileImage: MultipartBody.Part,
        name: RequestBody,
        phone:RequestBody,
        description: RequestBody,
        location: RequestBody,
        gender: RequestBody,
    ) {
        mainRepository.editBiodata(token, coverImage, profileImage , name, phone, description, location, gender)
    }
}
