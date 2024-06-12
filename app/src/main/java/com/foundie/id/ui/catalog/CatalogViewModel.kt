package com.foundie.id.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foundie.id.data.local.repository.MainRepository
import com.foundie.id.data.local.response.ProductData

class CatalogViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val productStatus: LiveData<String> = mainRepository.message

    val isLoadingProduct: LiveData<Boolean> = mainRepository.isLoading

    val product: LiveData<List<ProductData>> = mainRepository.product

    var isErrorProduct: Boolean = false

    init {
        productStatus.observeForever { status ->
            isErrorProduct = status != ""
        }
    }

    fun getProduct() {
        mainRepository.getProduct()
    }
}
