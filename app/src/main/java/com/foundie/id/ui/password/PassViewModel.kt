package com.foundie.id.ui.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository


class PassViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val passwordStatus: LiveData<String> = mainRepository.passwordStatus

    val isLoadingPass: LiveData<Boolean> = mainRepository.isLoadingPass

    var isErrorPass: Boolean = false

    init {
        passwordStatus.observeForever { status ->
            isErrorPass = status != "Password added successfully"
        }
    }

    fun setPassword(token: String, email: String, password: String) {
        mainRepository.setPassword(token, email, password)
    }
}