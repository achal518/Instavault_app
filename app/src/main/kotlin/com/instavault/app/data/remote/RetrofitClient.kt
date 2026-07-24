package com.instavault.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit Client — InstaVault Network Layer
 *
 * Provides a single, reusable Retrofit instance configured with:
 *   - 10-second connect/read/write timeouts
 *   - OkHttp logging interceptor for Logcat debugging
 *   - Gson converter for JSON serialization/deserialization
 *
 * BASE_URL points to 10.0.2.2 (Android emulator → host machine loopback).
 * In production, this will be replaced with the deployed server URL.
 */
object RetrofitClient {

    // 10.0.2.2 is the Android emulator's alias for host machine's localhost
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Provides the ApiService singleton instance.
     * Usage: `RetrofitClient.apiService.verifyVaultId(...)`
     */
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
