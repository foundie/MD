package com.foundie.id.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.User
import okhttp3.MultipartBody

class ProfileViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val profileStatus: LiveData<String> = mainRepository.message
    val editprofileStatus: LiveData<String> = mainRepository.editbiodatastatus

    val isLoadingProfile: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingeditBiodata: LiveData<Boolean> = mainRepository.isLoadingeditBiodata

    var isErrorProfile: Boolean = false
    var isErroreditBiodata: Boolean = false

    init {
        profileStatus.observeForever { status ->
            isErrorProfile = status != ""
        }
    }

    val biodata: LiveData<User> = mainRepository.biodata

    fun getBiodata(token: String) {
        mainRepository.getBiodata(token)
    }

    fun editBiodata(
        token: String,
        coverImage: MultipartBody.Part,
        profileImage: MultipartBody.Part,
        name: String,
        phone: String,
        location: String,
        gender: String,
    ) {
        mainRepository.editBiodata(token, coverImage, profileImage , name, phone, location, gender)
    }
}
