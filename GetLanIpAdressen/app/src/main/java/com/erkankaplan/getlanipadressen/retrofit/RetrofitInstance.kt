package com.erkankaplan.getlanipadressen.retrofit

import com.erkankaplan.getlanipadressen.retrofit._01_api_service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.125:4200/"
    private lateinit var api: ApiService
   /* api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }*/

    fun getInstance(): ApiService {
        return if(this::api.isInitialized){
            api
        } else{
            api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
            api
        }
    }


}