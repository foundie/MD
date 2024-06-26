package com.foundie.id.data.local.retrofit

import com.foundie.id.data.local.response.AddPasswordResponse
import com.foundie.id.data.local.response.AddPostUserResponse
import com.foundie.id.data.local.response.ColorAnalysisResponse
import com.foundie.id.data.local.response.CommunityUserResponse
import com.foundie.id.data.local.response.CompareProductResponse
import com.foundie.id.data.local.response.CreateCommunityResponse
import com.foundie.id.data.local.response.EditProfileResponse
import com.foundie.id.data.local.response.FilterProductResponse
import com.foundie.id.data.local.response.FollowResponse
import com.foundie.id.data.local.response.GroupCommunityResponse
import com.foundie.id.data.local.response.HistoryResponse
import com.foundie.id.data.local.response.LoginGoogleResponse
import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.response.MakeUpResponse
import com.foundie.id.data.local.response.ProductResponse
import com.foundie.id.data.local.response.RegisterResponse
import com.foundie.id.data.local.response.SkinToneResponse
import com.foundie.id.data.local.response.UserDetailResponse
import com.foundie.id.data.local.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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

    // Edit Biodata User
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
    @GET("products?limit=50&skip=20")
    fun getProduct(
        @Header("Authorization") token: String
    ): Call<ProductResponse>

    // Get Post Users
    @GET("community")
    fun getPostUser(
        @Header("Authorization") token: String
    ): Call<CommunityUserResponse>

    // Add Post Users
    @Multipart
    @POST("/community/post")
    fun addPostUser(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("text") text: RequestBody,
    ): Call<AddPostUserResponse>

    // Predict MakeupStyle
    @Multipart
    @POST("predict/face")
    fun styleMakeup(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
    ): Call<MakeUpResponse>

    // Predict SkinTone
    @Multipart
    @POST("predict/skin")
    fun skintone(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
    ): Call<SkinToneResponse>

    // Filter Products
    @Multipart
    @POST("/products/filter")
    fun filterProduct(
        @Header("Authorization") token: String,
        @Part("product_title") title: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("brand") brand: RequestBody?,
        @Part("variant_name") variant: RequestBody?
    ): Call<FilterProductResponse>

    // Get Compare Product
    @GET("products/compare/")
    fun getCompare(
        @Header("Authorization") token: String,
        @Query("index") index: Int
    ): Call<CompareProductResponse>

    // Get History Predict Users
    @GET("predict/history")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    data class ColorAnalysisRequest(
        val brightnessLevel: Int,
        val bluePreferences: Int,
        val yellowPreferences: Int,
        val greenPreferences: Int,
        val pinkPreferences: Int,
        val brownPreferences: Int,
        val clarityLevel: Int
    )

    @POST("/predict/color")
    fun colorAnalysis(
        @Header("Authorization") token: String,
        @Body request: ColorAnalysisRequest
    ): Call<ColorAnalysisResponse>

    // Get Email Detail
    @GET("biodata/{email}")
    fun getdetailUser(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Call<UserDetailResponse>

    // Follow User
    @POST("follow//{followingemail}")
    @FormUrlEncoded
    fun getfollow(
        @Path("followingemail") following: String,
    ): Call<FollowResponse>

    // Create Community Group
    @Multipart
    @POST("/community/create")
    fun createCommunity(
        @Header("Authorization") token: String,
        @Part coverImage: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("topics") topics: RequestBody,
        @Part("description") description: RequestBody,
    ): Call<CreateCommunityResponse>


    // Get Community Group
    @GET("community/group/search")
    fun getGroup(
        @Header("Authorization") token: String
    ): Call<GroupCommunityResponse>
}