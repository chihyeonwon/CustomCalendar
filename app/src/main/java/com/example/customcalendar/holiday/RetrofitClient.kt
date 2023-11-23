package com.example.customcalendar.holiday

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {
    private const val BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/"
    private var instance: Retrofit? = null

    open fun getInstance() : Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        } // end if

        return instance!!
    }
}