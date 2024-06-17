package com.foundie.id.ui.home.makeup_analysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.MakeUpStyleResponse
import com.foundie.id.data.local.response.PredictDataItem
import com.foundie.id.data.local.response.PredictResponse
import com.foundie.id.data.local.response.User
import okhttp3.MultipartBody

class PredictViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val makeUpStyleStatus: LiveData<String> = mainRepository.predictStatus
    val historyStatus: LiveData<String> = mainRepository.message

    val isLoadingmakeUpStyle: LiveData<Boolean> = mainRepository.isLoadingPredict
    val isLoadingHistory: LiveData<Boolean> = mainRepository.isLoading

    var isErrorPredict: Boolean = false
    var isErrorHistory: Boolean = false

    init {

        makeUpStyleStatus.observeForever { status ->
            isErrorPredict = status != ""
        }
        historyStatus.observeForever { status ->
            isErrorHistory = status != ""
        }
    }

    val history:  LiveData<PredictDataItem> = mainRepository.history

    fun makeupStyle(
        token: String,
        photo: MultipartBody.Part,
    ) {
        mainRepository.styleMakeup(token, photo)
    }

    fun getHistory(token: String){
        mainRepository.getHistory(token)
    }
}
