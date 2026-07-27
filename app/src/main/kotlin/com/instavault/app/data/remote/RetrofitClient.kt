package com.instavault.app.data.remote

import android.content.Context
import com.instavault.app.data.local.SessionExpiryNotifier
import com.instavault.app.data.local.SessionManager
import okhttp3.Interceptor
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
 * BASE_URL points to the deployed InstaVault API server.
 */
object RetrofitClient {

    private const val BASE_URL = "http://51.20.42.185:3000/"
    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val HEADER_VAULT_ID = "X-Vault-ID"

    @Volatile
    private var sessionManager: SessionManager? = null

    fun initialize(context: Context) {
        if (sessionManager != null) return

        synchronized(this) {
            if (sessionManager == null) {
                sessionManager = SessionManager(context.applicationContext)
            }
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val activeSession = sessionManager
        val token = activeSession?.getSessionToken()
        val vaultId = activeSession?.getVaultId()
        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrBlank() && chain.request().header(HEADER_AUTHORIZATION).isNullOrBlank()) {
            requestBuilder.header(HEADER_AUTHORIZATION, "Bearer $token")
        }

        if (!vaultId.isNullOrBlank() && chain.request().header(HEADER_VAULT_ID).isNullOrBlank()) {
            requestBuilder.header(HEADER_VAULT_ID, vaultId)
        }

        val hadSessionHeaders = !token.isNullOrBlank() || !vaultId.isNullOrBlank()
        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 && hadSessionHeaders) {
            activeSession?.clearSession()
            SessionExpiryNotifier.notifySessionExpired()
        }

        response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
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

    fun getApiService(context: Context): ApiService {
        initialize(context)
        return apiService
    }
}
