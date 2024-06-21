package com.foundie.id.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.foundie.id.settings.SettingsPreferences
import kotlinx.coroutines.launch

class AuthViewModel(private val prefen: SettingsPreferences) : ViewModel() {

    fun getUserLoginSession(): LiveData<Boolean> {
        return prefen.getUserLoginSession().asLiveData()
    }

    fun saveUserLoginSession(userloginSession: Boolean) {
        viewModelScope.launch {
            prefen.saveUserLoginSession(userloginSession)
        }
    }

    fun getUserLoginToken(): LiveData<String> {
        return prefen.getUserLoginToken().asLiveData()
    }

    fun saveUserLoginToken(token: String) {
        viewModelScope.launch {
            prefen.saveUserLoginToken(token)
        }
    }

    fun getUserLoginName(): LiveData<String> {
        return prefen.getUserloginName().asLiveData()
    }

    fun saveUserLoginName(token: String) {
        viewModelScope.launch {
            prefen.saveUserLoginName(token)
        }
    }

    fun getUserLoginRole(): LiveData<String> {
        return prefen.getUserloginRole().asLiveData()
    }

    fun saveUserLoginRole(token: String) {
        viewModelScope.launch {
            prefen.saveUserLoginRole(token)
        }
    }

    fun getUserLoginEmail(): LiveData<String> {
        return prefen.getUserloginEmail().asLiveData()
    }

    fun saveUserLoginEmail(token: String) {
        viewModelScope.launch {
            prefen.saveUserLoginEmail(token)
        }
    }

    fun getUserLastLoginSession(): LiveData<String> {
        return prefen.getUserLastLoginSession().asLiveData()
    }

    fun saveUserLastLoginSession(token: String) {
        viewModelScope.launch {
            prefen.saveUserLastLoginSession(token)
        }
    }

    fun deleteUserLoginSession() {
        viewModelScope.launch {
            prefen.deleteUserLoginSession()
        }
    }

}