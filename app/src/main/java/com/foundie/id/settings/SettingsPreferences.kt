package com.foundie.id.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserLoginSession(): Flow<Boolean> {
        return dataStore.data.map { prefen ->
            prefen[USER_LOGIN_SESSION] ?: false
        }
    }

    suspend fun saveUserLoginSession(userloginSession: Boolean) {
        dataStore.edit { prefen ->
            prefen[USER_LOGIN_SESSION] = userloginSession
        }
    }

    fun getUserLoginToken(): Flow<String> {
        return dataStore.data.map { prefen ->
            prefen[USER_LOGIN_TOKEN] ?: ""
        }
    }

    suspend fun saveUserLoginToken(token: String) {
        dataStore.edit { prefen ->
            prefen[USER_LOGIN_TOKEN] = token
        }
    }

    fun getUserloginName(): Flow<String> {
        return dataStore.data.map { prefen ->
            prefen[USER_LOGIN_NAME] ?: ""
        }
    }

    suspend fun saveUserLoginName(userloginName: String) {
        dataStore.edit { prefen ->
            prefen[USER_LOGIN_NAME] = userloginName
        }
    }

    fun getUserloginRole(): Flow<String> {
        return dataStore.data.map { prefen ->
            prefen[USER_LOGIN_ROLE] ?: ""
        }
    }

    suspend fun saveUserLoginRole(userloginRole: String) {
        dataStore.edit { prefen ->
            prefen[USER_LOGIN_ROLE] = userloginRole
        }
    }

    fun getUserloginEmail(): Flow<String> {
        return dataStore.data.map { prefen ->
            prefen[USER_LOGIN_EMAIL] ?: ""
        }
    }

    suspend fun saveUserLoginEmail(userloginEmail: String) {
        dataStore.edit { prefen ->
            prefen[USER_LOGIN_EMAIL] = userloginEmail
        }
    }

    fun getUserLastLoginSession(): Flow<String> {
        return dataStore.data.map { prefen ->
            prefen[USER_LOGIN_LAST_SESSION] ?: ""
        }
    }

    suspend fun saveUserLastLoginSession(userlastloginSession: String) {
        dataStore.edit { prefen ->
            prefen[USER_LOGIN_LAST_SESSION] = userlastloginSession
        }
    }

    suspend fun deleteUserLoginSession() {
        dataStore.edit { prefen ->
            prefen.remove(USER_LOGIN_SESSION)
            prefen.remove(USER_LOGIN_TOKEN)
            prefen.remove(USER_LOGIN_EMAIL)
            prefen.remove(USER_LOGIN_NAME)
            prefen.remove(USER_LOGIN_ROLE)
            prefen.remove(USER_LOGIN_LAST_SESSION)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val USER_LOGIN_SESSION = booleanPreferencesKey("user_login_session")
        private val USER_LOGIN_TOKEN = stringPreferencesKey("user_login_token")
        private val USER_LOGIN_NAME = stringPreferencesKey("user_login_name")
        private val USER_LOGIN_ROLE = stringPreferencesKey("user_login_role")
        private val USER_LOGIN_EMAIL = stringPreferencesKey("user_login_email")
        private val USER_LOGIN_LAST_SESSION = stringPreferencesKey("user_login_last_session")

    }
}