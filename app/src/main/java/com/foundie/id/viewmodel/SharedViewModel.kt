package com.foundie.id.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.response.MakeUpResponse

class SharedViewModel : ViewModel() {
    private val _predictResponse = MutableLiveData<MakeUpResponse>()
    val predictResponse: LiveData<MakeUpResponse> = _predictResponse

    fun setPredictResponse(response: MakeUpResponse) {
        _predictResponse.value = response
    }
}
