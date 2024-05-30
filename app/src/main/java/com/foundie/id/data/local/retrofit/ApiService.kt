package com.foundie.id.data.local.retrofit

import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

}