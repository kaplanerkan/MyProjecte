package com.erkankaplan.getlanipadressen.retrofit._01_api_service

import com.erkankaplan.getlanipadressen.room._01_entity.ProductModel
import retrofit2.http.GET

interface ApiService {
//
//    @GET("api/Synchronize/SynchronizeDatas")
//    suspend fun getProducts() : List<ProductModel>


    // Hepsini ApiResponse den tutuyoz
    @GET("api/Synchronize/SynchronizeDatas")
    suspend fun getApiResponse(): ApiResponse


}