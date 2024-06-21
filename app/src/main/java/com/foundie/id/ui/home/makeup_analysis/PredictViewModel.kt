package com.foundie.id.ui.home.makeup_analysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.DataAnalysis
import com.foundie.id.data.local.response.DataFilterProduct
import com.foundie.id.data.local.response.PredictDataItem
import com.foundie.id.data.local.response.SimilarProductsItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PredictViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val makeUpStyleStatus: LiveData<String> = mainRepository.predictStatus
    val skinToneStyleStatus: LiveData<String> = mainRepository.predictStatus
    val historyStatus: LiveData<String> = mainRepository.message
    val filterStatus: LiveData<String> = mainRepository.message
    val compareStatus: LiveData<String> = mainRepository.message
    val colorAnalysisStatus: LiveData<String> = mainRepository.message

    val isLoadingmakeUpStyle: LiveData<Boolean> = mainRepository.isLoadingPredict
    val isLoadingskinTone: LiveData<Boolean> = mainRepository.isLoadingPredict
    val isLoadingHistory: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingFilter: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingCompare: LiveData<Boolean> = mainRepository.isLoading
    val isLoadingColorAnalysis: LiveData<Boolean> = mainRepository.isLoading

    var isErrorPredict: Boolean = false
    var isErrorHistory: Boolean = false
    var isErrorFilter: Boolean = false
    var isErrorCompare: Boolean = false
    var isErrorColors: Boolean = false

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
        compareStatus.observeForever { status ->
            isErrorCompare = status != ""
        }
        colorAnalysisStatus.observeForever { status ->
            isErrorColors= status != ""
        }
    }

    val historyMakeUp:  LiveData<PredictDataItem> = mainRepository.historyMakeUp
    val historySkinTone:  LiveData<PredictDataItem> = mainRepository.historySkinTone
    val historyColorAnalysis:  LiveData<PredictDataItem> = mainRepository.historyColorAnalysis
    val filterProduct:  LiveData<List<DataFilterProduct>> = mainRepository.filterProduct
    val compareProduct: LiveData<List<SimilarProductsItem>> = mainRepository.compareProduct
    val colorAnalysis: LiveData<DataAnalysis> = mainRepository.colorAnalysis


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

    fun getCompare(token: String, index: Int){
        mainRepository.getCompare(token,index)
    }

    fun colorAnalysis(token: String, bright:Int, blue:Int, yellow:Int, green:Int, pink:Int, brown:Int, clarity:Int) {
        mainRepository.colorAnalysis(token,bright,blue,yellow,green,pink,brown,clarity)
    }
}
