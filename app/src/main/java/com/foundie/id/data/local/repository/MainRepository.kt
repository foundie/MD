package com.foundie.id.data.local.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foundie.id.data.local.response.AddPasswordResponse
import com.foundie.id.data.local.response.CommunityUserResponse
import com.foundie.id.data.local.response.DataPostItem
import com.foundie.id.data.local.response.EditProfileResponse
import com.foundie.id.data.local.response.LoginGoogleResponse
import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.response.PredictResponse
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.data.local.response.ProductResponse
import com.foundie.id.data.local.response.RegisterResponse
import com.foundie.id.data.local.response.User
import com.foundie.id.data.local.response.UserResponse
import com.foundie.id.data.local.retrofit.ApiService
import com.foundie.id.settings.wrapEspressoIdlingResource
import com.foundie.id.data.local.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(private val apiService: ApiService) {

    private val _isLoadingRegister = MutableLiveData<Boolean>()
    val isLoadingRegister: LiveData<Boolean> = _isLoadingRegister
    private val _registerStatus = MutableLiveData<String>()
    val registerStatus: LiveData<String> = _registerStatus
    var isErrorRegister: Boolean = false

    private val _isLoadingPass = MutableLiveData<Boolean>()
    val isLoadingPass: LiveData<Boolean> = _isLoadingPass
    private val _passwordStatus = MutableLiveData<String>()
    val passwordStatus: LiveData<String> = _passwordStatus
    var isErrorPassword: Boolean = false

    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus
    private val _login = MutableLiveData<LoginResponse>()
    val loginUser: LiveData<LoginResponse> = _login
    var isErrorLogin: Boolean = false

    private val _isLoadingVerify = MutableLiveData<Boolean>()
    val isLoadingVerify: LiveData<Boolean> = _isLoadingVerify
    private val _verifyStatus = MutableLiveData<String>()
    val verifyStatus: LiveData<String> = _verifyStatus
    private val _verify = MutableLiveData<LoginGoogleResponse>()
    val verifyUser: LiveData<LoginGoogleResponse> = _verify
    var isErrorVerify: Boolean = false

    private val _predictStatus = MutableLiveData<String>()
    val predictStatus: LiveData<String> = _predictStatus
    private val _isLoadingpredict = MutableLiveData<Boolean>()
    val isLoadingpredict: LiveData<Boolean> = _isLoadingpredict
    private val _makeupStyle = MutableLiveData<PredictResponse>()
    val predict: LiveData<PredictResponse> = _makeupStyle
    var isErrorpredict: Boolean = false

    private val _editbiodatastatus = MutableLiveData<String>()
    val editbiodatastatus: LiveData<String> = _editbiodatastatus
    private val _isLoadingeditBiodata = MutableLiveData<Boolean>()
    val isLoadingeditBiodata: LiveData<Boolean> = _isLoadingeditBiodata
    var isErroreditBiodata: Boolean = false


    private val _product = MutableLiveData<List<ProductData>>()
    val product: LiveData<List<ProductData>> = _product

    private val _postuser = MutableLiveData<List<DataPostItem>>()
    val postuser: LiveData<List<DataPostItem>> = _postuser

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    private val _biodata = MutableLiveData<User>()
    val biodata: LiveData<User> = _biodata
    //var product: List<ProductData> = listOf()
    var isError: Boolean = false


    fun register(name: String, email: String, password: String) {
        _isLoadingRegister.value = true
        val api = ApiConfig.getApiService().register(name, email, password)
        api.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoadingRegister.value = false
                if (response.isSuccessful) {
                    isErrorRegister = false
                    _registerStatus.value = "Account successfully created"
                } else {
                    isErrorRegister = true
                    when (response.code()) {
                        409 -> _registerStatus.value =
                            "Email already exists"
                        408 -> _registerStatus.value =
                            "Your internet connection is slow, please try again"
                        500 -> _registerStatus.value =
                            "Server not found, please try again later"
                        else -> {
                            _registerStatus.value = response.message()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                isErrorRegister = true
                _isLoadingRegister.value = false
                _registerStatus.value = t.message.toString()
            }

        })
    }

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
                        isErrorLogin = false
                        _login.value = responseBody!!
                        _loginStatus.value =
                            "Welcome ${_login.value!!.loginResult.name}, To Foundie"
                    } else {
                        isErrorLogin = true
                        when (response.code()) {
                            401 -> _loginStatus.value = "User not found"
                            403 -> _loginStatus.value = "You have reached the maximum number of login attempts. Please try again later."
                            500 -> _loginStatus.value = "Server not found. Please try again later."
                            else -> {
                                _loginStatus.value = response.message()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    isErrorLogin = true
                    _isLoadingLogin.value = false
                    _loginStatus.value = t.message.toString()
                }

            })
        }
    }

    fun verify(token: String) {
        wrapEspressoIdlingResource {
            _isLoadingVerify.value = true
            val api = ApiConfig.getApiService().lGoogle(token)
            api.enqueue(object : Callback<LoginGoogleResponse> {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<LoginGoogleResponse>,
                    response: Response<LoginGoogleResponse>
                ) {
                    val responseBody = response.body()
                    _isLoadingVerify.value = false
                    if (response.isSuccessful && responseBody != null) {
                        isErrorVerify = false
                        _verify.value = responseBody
                        _verifyStatus.value = "Welcome ${responseBody.loginGoogleResult.name}, To Foundie"
                    } else {
                        isErrorVerify = true
                        when (response.code()) {
                            401 -> _verifyStatus.value = "Invalid Google token"
                            500 -> _verifyStatus.value = "Server not found. Please try again later."
                            else -> _verifyStatus.value = response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginGoogleResponse>, t: Throwable) {
                    isErrorVerify = true
                    _isLoadingVerify.value = false
                    _verifyStatus.value = t.message.toString()
                }
            })
        }
    }

    fun setPassword(token: String,email: String,password: String) {
        wrapEspressoIdlingResource {
            _isLoadingPass.value = true
            val api = ApiConfig.getApiService().setPassword("Bearer $token",email,password)
            api.enqueue(object : Callback<AddPasswordResponse> {
                override fun onResponse(
                    call: Call<AddPasswordResponse>,
                    response: Response<AddPasswordResponse>
                ) {
                    _isLoadingPass.value = false
                    if (response.isSuccessful) {
                        isErrorPassword= false
                        _passwordStatus.value = "Password added successfully"
                    } else {
                        isErrorPassword = true
                        when (response.code()) {
                            401 -> _passwordStatus.value =
                                "Unauthorized"
                            408 -> _passwordStatus.value =
                                "Your internet connection is slow, please try again"
                            500 -> _passwordStatus.value =
                                "Server not found, please try again later"
                            else -> {
                                _passwordStatus.value = response.message()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AddPasswordResponse>, t: Throwable) {
                    isErrorPassword = true
                    _isLoadingPass.value = false
                    _passwordStatus.value = t.message.toString()
                }

            })
        }
    }

    fun getBiodata(token: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getBiodata("Bearer $token")
        api.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _biodata.value = responseBody.user
                    }
                    _message.value = responseBody?.message.toString()
                } else {
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message.toString()
            }
        })
    }

    fun editBiodata(token: String, coverImage: MultipartBody.Part, profileImage: MultipartBody.Part, name: RequestBody, phone: RequestBody,  description: RequestBody, location: RequestBody, gender: RequestBody) {
        _isLoadingeditBiodata.value = true
        val service = ApiConfig.getApiService().editBiodata(
            "Bearer $token", coverImage, profileImage,name,phone,description,location,gender
        )
        service.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                _isLoadingeditBiodata.value = false
                if (response.isSuccessful) {
                    isErroreditBiodata = false
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _editbiodatastatus.value = responseBody.message
                    }
                } else {
                    isErroreditBiodata = true
                    _editbiodatastatus.value = response.message()

                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                _isLoadingeditBiodata.value = false
                isErroreditBiodata = true
                _editbiodatastatus.value = t.message
            }
        })
    }

    fun getProduct() {
        _isLoading.value = true
        val api = ApiConfig.getApiService2().getProduct()
        api.enqueue(object : retrofit2.Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _product.value = responseBody.data
                    }
                    _message.value = responseBody?.message.toString()
                } else {
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message.toString()
            }
        })
    }

    fun getPostUser(token: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getPostUser("Bearer $token")
        api.enqueue(object : retrofit2.Callback<CommunityUserResponse> {
            override fun onResponse(call: Call<CommunityUserResponse>, response: Response<CommunityUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _postuser.value = responseBody.data
                    }
                    _message.value = responseBody?.message.toString()
                } else {
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<CommunityUserResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message.toString()
            }
        })
    }

    fun styleMakeup(token: String, photo: MultipartBody.Part) {
        _isLoadingpredict.value = true
        val service = ApiConfig.getApiService().styleMakeup(
            "Bearer $token", photo)
        service.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                val responseBody = response.body()
                _isLoadingpredict.value = false
                if (response.isSuccessful) {
                    isErrorpredict = false
                    _makeupStyle.value = responseBody!!
                    _predictStatus.value =
                        "Success Get MakeUpStyle Recomendation"
                } else {
                    isErrorpredict = true
                    when (response.code()) {
                        401 -> _predictStatus.value = "Data not found"
                        408 -> _passwordStatus.value = "Your internet connection is slow, please try again"
                        500 -> _predictStatus.value = "Server not found. Please try again later."
                        else -> {
                            _predictStatus.value = response.message()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                isErrorpredict = true
                _isLoadingpredict.value = false
                _predictStatus.value = t.message.toString()
            }

        })
    }
}