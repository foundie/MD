package com.foundie.id.di

import android.content.Context
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService()
        return MainRepository(apiService)
    }
}