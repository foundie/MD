package com.foundie.id.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.retrofit.ApiService
import com.foundie.id.settings.wrapEspressoIdlingResource
import com.foundie.id.data.local.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(private val apiService: ApiService) {

    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus
    private val _login = MutableLiveData<LoginResponse>()
    val loginUser: LiveData<LoginResponse> = _login

    fun login(email: String, password: String) {
        wrapEspressoIdlingResource {
            _isLoadingLogin.value = true
            val api = ApiConfig.getApiService().login(email, password)
            api.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val responseBody = response.body()
                    _isLoadingLogin.value = false
                    if (response.isSuccessful) {
                        _login.value = responseBody!!
                        _loginStatus.value =
                            "Welcome ${_login.value!!.loginResult.name}, To Foundie"
                    } else {
                        when (response.code()) {
                            401 -> _loginStatus.value = "User not found"
                            500 -> _loginStatus.value = "Server not found. Please try again later."
                            else -> {
                                _loginStatus.value = response.message()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoadingLogin.value = false
                    _loginStatus.value = t.message.toString()
                }

            })
        }
    }
}