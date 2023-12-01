package com.apptrick.app.data.repo

import com.apptrick.app.data.api.helpers.Result
import com.apptrick.app.data.api.helpers.safeApiCall
import com.apptrick.app.data.api.model.responsemodels.ProductResponse
import com.apptrick.app.data.api.service.HomeService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepository @Inject constructor(private val service: HomeService) {
    fun products(): Flow<Result<ProductResponse>> {
        return safeApiCall { service.products() }
    }
}