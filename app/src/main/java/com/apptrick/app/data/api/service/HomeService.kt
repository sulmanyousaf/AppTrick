package com.apptrick.app.data.api.service

import com.apptrick.app.data.api.helpers.ApiNames
import com.apptrick.app.data.api.model.responsemodels.ProductResponse
import retrofit2.http.GET

interface HomeService {

    @GET(ApiNames.PRODUCTS)
    suspend fun products(): ProductResponse
}