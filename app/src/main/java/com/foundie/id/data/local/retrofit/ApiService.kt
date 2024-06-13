package com.foundie.id.data.local.retrofit

import com.foundie.id.data.local.response.AddPasswordResponse
import com.foundie.id.data.local.response.CommunityUserResponse
import com.foundie.id.data.local.response.EditProfileResponse
import com.foundie.id.data.local.response.LoginGoogleResponse
import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.response.PredictResponse
import com.foundie.id.data.local.response.ProductResponse
import com.foundie.id.data.local.response.RegisterResponse
import com.foundie.id.data.local.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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
    @POST("biodata/add-password")
    @FormUrlEncoded
    fun setPassword(
        @Header("Authorization") token: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<AddPasswordResponse>

    // Get Biodata User
    @GET("biodata/me")
    fun getBiodata(
        @Header("Authorization") token: String,
    ): Call<UserResponse>

    @Multipart
    @POST("biodata/add")
    fun editBiodata(
        @Header("Authorization") token: String,
        @Part coverImage: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("description") description: RequestBody,
        @Part("location") location: RequestBody,
        @Part("gender") gender: RequestBody,
    ): Call<EditProfileResponse>

    // Get Products
    @GET("products")
    fun getProduct(
    ): Call<ProductResponse>

    // Get Post Users
    @GET("community")
    fun getPostUser(
        @Header("Authorization") token: String
    ): Call<CommunityUserResponse>

    @Multipart
    @POST("predict/face")
    fun styleMakeup(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Call<PredictResponse>

}