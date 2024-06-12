package com.foundie.id.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.LoginResponse
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.data.local.response.ProductResponse

class CatalogViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val productStatus: LiveData<String> = mainRepository.message

    val isLoadingProduct: LiveData<Boolean> = mainRepository.isLoading

    var isErrorProduct: Boolean = false

    init {
        productStatus.observeForever { status ->
            isErrorProduct = status != ""
        }
    }

    val product: List<ProductData> = mainRepository.product

    fun getProduct() {
        mainRepository.getProduct()
    }
}
