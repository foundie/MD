package com.foundie.id.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.response.PredictResponse

class SharedViewModel : ViewModel() {
    private val _predictResponse = MutableLiveData<PredictResponse>()
    val predictResponse: LiveData<PredictResponse> = _predictResponse

    fun setPredictResponse(response: PredictResponse) {
        _predictResponse.value = response
    }
}
