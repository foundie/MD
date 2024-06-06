package com.foundie.id.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.LoginGoogleResponse

class VerifyViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val verifyStatus: LiveData<String> = mainRepository.verifyStatus

    val isLoadingVerify: LiveData<Boolean> = mainRepository.isLoadingVerify

    var isErrorVerify: Boolean = false

    init {
        verifyStatus.observeForever { status ->
            isErrorVerify = status != "Welcome ${verify.value?.loginGoogleResult?.name}, To Foundie"
        }
    }

    val verify: LiveData<LoginGoogleResponse> = mainRepository.verifyUser

    fun verification(idtoken: String) {
        mainRepository.verify(idtoken)
    }
}