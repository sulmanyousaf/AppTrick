package com.apptrick.app.ui.fragment.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.apptrick.app.data.repo.HomeRepository
import com.apptrick.app.ui.fragment.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.apptrick.app.data.api.helpers.Result
import com.apptrick.app.data.api.model.responsemodels.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeFragmentVM @Inject constructor(private val repository: HomeRepository) : BaseViewModel() {

    private val _productsList = MutableStateFlow<List<ProductResponse.Products>>(emptyList())
    val productsList = _productsList.asStateFlow()

    init {
        products()
    }

    private fun products() {
        showLoader()
        repository.products().onEach { result ->
            when (result) {
                is Result.Error -> {
                    sendError(result.message)
                    Log.e("ApiResponse", "products Error ${result.message}")
                }

                is Result.Success -> {
                    hideLoader()
                    Log.e("ApiResponse", "products success")
                    _productsList.value = result.data.products
                }
            }
        }.launchIn(viewModelScope)
    }
}