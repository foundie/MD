package com.foundie.id.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository


class SignUpViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val registerStatus: LiveData<String> = mainRepository.registerStatus

    val isLoadingRegister: LiveData<Boolean> = mainRepository.isLoadingRegister

    var isErrorRegister: Boolean = false

    init {
        registerStatus.observeForever { status ->
            isErrorRegister = status != "Account successfully created"
        }
    }

    fun register(name: String, email: String, password: String) {
        mainRepository.register(name, email, password)
    }
}