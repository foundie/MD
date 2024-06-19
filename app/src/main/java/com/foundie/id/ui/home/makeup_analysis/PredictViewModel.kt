package com.foundie.id.ui.home.makeup_analysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.DataFilterProduct
import com.foundie.id.data.local.response.PredictDataItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PredictViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val makeUpStyleStatus: LiveData<String> = mainRepository.predictStatus
    val skinToneStyleStatus: LiveData<String> = mainRepository.predictStatus
    val historyStatus: LiveData<String> = mainRepository.message
    val filterStatus: LiveData<String> = mainRepository.message

    val isLoadingmakeUpStyle: LiveData<Boolean> = mainRepository.isLoadingPredict
    val isLoadingskinTone: LiveData<Boolean> = mainRepository.isLoadingPredict
    val isLoadingHistory: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingFilter: LiveData<Boolean> = mainRepository.isLoading

    var isErrorPredict: Boolean = false
    var isErrorHistory: Boolean = false
    var isErrorFilter: Boolean = false

    init {

        makeUpStyleStatus.observeForever { status ->
            isErrorPredict = status != ""
        }
        skinToneStyleStatus.observeForever { status ->
            isErrorPredict = status != ""
        }
        historyStatus.observeForever { status ->
            isErrorHistory = status != ""
        }
        filterStatus.observeForever { status ->
            isErrorFilter = status != ""
        }
    }

    val historyMakeUp:  LiveData<PredictDataItem> = mainRepository.historyMakeUp
    val historySkinTone:  LiveData<PredictDataItem> = mainRepository.historySkinTone
    val filterProduct:  LiveData<List<DataFilterProduct>> = mainRepository.filterProduct

    fun makeupStyle(
        token: String,
        photo: MultipartBody.Part,
    ) {
        mainRepository.styleMakeup(token, photo)
    }

    fun skintone(
        token: String,
        photo: MultipartBody.Part,
    ) {
        mainRepository.skintone(token, photo)
    }

    fun filter(token: String, title: RequestBody?, type: RequestBody?, brand: RequestBody?, variant: RequestBody?) {
        mainRepository.filterProduct(token,title,type,brand,variant)
    }

    fun getHistory(token: String){
        mainRepository.getHistory(token)
    }
}
