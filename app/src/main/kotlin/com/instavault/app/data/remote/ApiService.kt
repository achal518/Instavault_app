package com.instavault.app.data.remote

import com.instavault.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit API Interface — InstaVault App Server
 *
 * Defines all server endpoints as Kotlin suspend functions.
 * Each function maps directly to an endpoint we built in the Node.js backend.
 */
interface ApiService {

    /**
     * Step 1 of Auth Flow — Validate Vault ID against Firestore.
     * Server returns a secure nonce if the ID exists.
     */
    @POST("auth/verify-vault-id")
    suspend fun verifyVaultId(
        @Body request: VerifyVaultIdRequest
    ): Response<VerifyVaultIdResponse>

    /**
     * Step 3 of Auth Flow — Verify Play Integrity token and create session.
     * Server returns session_token + user_profile on success.
     */
    @POST("auth/verify-integrity")
    suspend fun verifyIntegrity(
        @Body request: VerifyIntegrityRequest
    ): Response<VerifyIntegrityResponse>

    /**
     * Step 5 of Auth Flow — Background telemetry sync (fire-and-forget).
     * Protected by auth headers (session token + vault ID).
     */
    @POST("telemetry/log-session")
    suspend fun logSessionTelemetry(
        @Header("Authorization") authHeader: String,
        @Header("X-Vault-ID") vaultId: String,
        @Body payload: TelemetryPayload
    ): Response<Unit>
}
