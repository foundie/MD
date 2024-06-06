package com.foundie.id.data.local.retrofit

import com.foundie.id.data.local.response.AddPasswordResponse
import com.foundie.id.data.local.response.LoginGoogleResponse
import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.response.MakeUpStyleResponse
import com.foundie.id.data.local.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    // API REGISTER
    @POST("register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    // API Login
    @POST("auth/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // API Login Verify Google
    @POST("auth/verify")
    @FormUrlEncoded
    fun lGoogle(
        @Field("token") token: String,
    ): Call<LoginGoogleResponse>

    // Set Password Login Google
    @POST("/biodata/add-password")
    @FormUrlEncoded
    fun setPassword(
        @Header("Authorization") token: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<AddPasswordResponse>

    @Multipart
    @POST("predict")
    fun styleMakeup(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Call<MakeUpStyleResponse>

}