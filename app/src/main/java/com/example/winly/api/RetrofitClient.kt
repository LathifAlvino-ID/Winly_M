package com.example.winly.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // EMULATOR: gunakan 10.0.2.2
    // HP FISIK: ganti dengan IP komputer kamu, contoh: "http://192.168.1.5/winly.api/"
    private const val BASE_URL = "http://10.0.2.2/winly.api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
