package com.foundie.id.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthModelFactory(private val prefen: SettingsPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(prefen) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}