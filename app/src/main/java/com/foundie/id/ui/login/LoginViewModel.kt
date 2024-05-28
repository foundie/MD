package com.foundie.id.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.LoginResponse

class LoginViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val loginStatus: LiveData<String> = mainRepository.loginStatus

    val isLoadingLogin: LiveData<Boolean> = mainRepository.isLoadingLogin

    init {
        loginStatus.observeForever { status ->
            isErrorLogin = status != "Welcome ${login.value?.loginResult?.name}, To Foundie"
        }
    }

    val login: LiveData<LoginResponse> = mainRepository.loginUser

    fun login(email: String, password: String) {
        mainRepository.login(email, password)
    }
}
