package com.instavault.app.data.repository

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import com.instavault.app.data.local.SessionManager
import com.instavault.app.data.remote.RetrofitClient
import com.instavault.app.data.remote.dto.*
import com.instavault.app.security.AttestationResult
import com.instavault.app.security.PlayIntegrityManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * AuthRepository — Central Authentication Coordinator
 *
 * This is the single point of contact between the ViewModel layer and all
 * underlying services (Network, Play Integrity, Encrypted Storage).
 *
 * The ViewModel never talks to Retrofit, PlayIntegrity, or SessionManager directly.
 * It only calls AuthRepository functions and receives clean Result objects.
 *
 * Orchestrates the full 5-step authentication flow defined in the Master Auth Plan:
 *   Step 1: Verify Vault ID → get nonce
 *   Step 2: Request Play Integrity token using nonce
 *   Step 3: Send integrity token to server → get session_token + user_profile
 *   Step 4: Save session locally in encrypted storage
 *   Step 5: Send background telemetry (fire-and-forget)
 */
class AuthRepository(private val context: Context) {

    private val apiService = RetrofitClient.apiService
    private val sessionManager = SessionManager(context)
    private val playIntegrityManager = PlayIntegrityManager.getInstance(context)

    companion object {
        private const val TAG = "AuthRepository"
    }

    /**
     * Step 1: Verify Vault ID against Firestore via server.
     * Returns the server-generated nonce on success.
     *
     * @param vaultId The user's Vault ID (e.g., "VLT-12345")
     * @return Result<String> containing the nonce on success
     */
    suspend fun verifyVaultId(vaultId: String): Result<String> {
        return try {
            Log.d(TAG, "Step 1: Verifying Vault ID: $vaultId")

            val response = apiService.verifyVaultId(VerifyVaultIdRequest(vaultId))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.valid && body.nonce != null) {
                    Log.d(TAG, "Step 1 SUCCESS: Nonce received")
                    Result.success(body.nonce)
                } else {
                    val errorMsg = body?.error ?: "Vault ID not found"
                    Log.w(TAG, "Step 1 FAILED: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Vault ID not found. Please check and try again."
                    429 -> "Too many attempts. Please wait and try again."
                    else -> "Server error (${response.code()}). Please try later."
                }
                Log.w(TAG, "Step 1 HTTP ERROR: ${response.code()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Step 1 EXCEPTION", e)
            Result.failure(Exception("Network error. Please check your internet connection."))
        }
    }

    /**
     * Full Login Flow — Orchestrates Steps 1 through 4.
     *
     * This is the primary function called by LoginViewModel.
     * It chains all authentication steps and returns the UserProfile on success.
     *
     * @param vaultId The user's Vault ID
     * @return Result<UserProfile> containing the user's profile data on success
     */
    suspend fun performFullLogin(vaultId: String): Result<UserProfile> {
        return try {
            // ── Step 1: Verify Vault ID → Get Nonce ──
            Log.d(TAG, "Starting full login flow for: $vaultId")

            val nonceResult = verifyVaultId(vaultId)
            if (nonceResult.isFailure) {
                return Result.failure(nonceResult.exceptionOrNull()!!)
            }
            val nonce = nonceResult.getOrThrow()

            // ── Step 2: Request Play Integrity Token using Nonce ──
            Log.d(TAG, "Step 2: Requesting Play Integrity token...")

            val attestationResult = playIntegrityManager.requestIntegrityToken(nonce)

            val integrityToken = when (attestationResult) {
                is AttestationResult.Success -> {
                    Log.d(TAG, "Step 2 SUCCESS: Integrity token received")
                    attestationResult.token
                }
                is AttestationResult.Error -> {
                    Log.e(TAG, "Step 2 FAILED: ${attestationResult.message}")
                    return Result.failure(Exception("Device verification failed: ${attestationResult.message}"))
                }
                is AttestationResult.Loading -> {
                    // This should not happen in a suspend function, but handle defensively
                    return Result.failure(Exception("Integrity check is still loading. Please try again."))
                }
            }

            // ── Step 3: Send Integrity Token to Server → Get Session + Profile ──
            Log.d(TAG, "Step 3: Sending integrity token to server...")

            val integrityResponse = apiService.verifyIntegrity(
                VerifyIntegrityRequest(
                    vaultId = vaultId,
                    integrityToken = integrityToken
                )
            )

            if (!integrityResponse.isSuccessful) {
                val errorMsg = when (integrityResponse.code()) {
                    403 -> "Device integrity check failed. This app cannot run on modified devices."
                    404 -> "User not found on server."
                    429 -> "Too many attempts. Please wait and try again."
                    else -> "Server error (${integrityResponse.code()}). Please try later."
                }
                Log.w(TAG, "Step 3 HTTP ERROR: ${integrityResponse.code()}")
                return Result.failure(Exception(errorMsg))
            }

            val integrityBody = integrityResponse.body()
            if (integrityBody?.sessionToken == null || integrityBody.userProfile == null) {
                val errorMsg = integrityBody?.error ?: "Invalid server response"
                Log.w(TAG, "Step 3 FAILED: $errorMsg")
                return Result.failure(Exception(errorMsg))
            }

            // ── Step 4: Save Session to Encrypted Local Storage ──
            Log.d(TAG, "Step 4: Saving session to encrypted storage...")

            sessionManager.saveSession(
                sessionToken = integrityBody.sessionToken,
                vaultId = vaultId,
                userProfile = integrityBody.userProfile
            )

            Log.d(TAG, "Login flow COMPLETE for: $vaultId")
            Result.success(integrityBody.userProfile)

        } catch (e: Exception) {
            Log.e(TAG, "Login flow EXCEPTION", e)
            Result.failure(Exception("Network error. Please check your internet connection."))
        }
    }

    /**
     * Step 5: Background Telemetry Sync (Fire-and-Forget).
     *
     * Collects device metrics and sends them to the server asynchronously.
     * Called after the user reaches the Dashboard. Failures are silently ignored
     * to ensure the app never lags or crashes because of telemetry.
     */
    suspend fun sendTelemetry() {
        try {
            val token = sessionManager.getSessionToken() ?: return
            val vaultId = sessionManager.getVaultId() ?: return

            val payload = TelemetryPayload(
                deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
                deviceBrand = Build.BRAND,
                osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
                appVersion = getAppVersion(),
                buildNumber = getAppVersionCode(),
                deviceLocale = Locale.getDefault().toString(),
                screenDensity = getScreenDensity(),
                networkType = "UNKNOWN", // Can be enhanced with ConnectivityManager later
                isEmulator = isRunningOnEmulator(),
                isRooted = false, // Can be enhanced with root detection library later
                clientTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date())
            )

            apiService.logSessionTelemetry(
                authHeader = "Bearer $token",
                vaultId = vaultId,
                payload = payload
            )

            Log.d(TAG, "Telemetry sent successfully")
        } catch (e: Exception) {
            // Silently ignore — telemetry must never crash the app
            Log.w(TAG, "Telemetry send failed (non-critical): ${e.message}")
        }
    }

    /**
     * Quick check: Is the user currently logged in with a valid local session?
     */
    fun isLoggedIn(): Boolean = sessionManager.hasValidSession()

    /**
     * Returns the cached UserProfile from encrypted storage.
     */
    fun getCachedProfile(): UserProfile? = sessionManager.getUserProfile()

    /**
     * Clears all session data (Logout).
     */
    fun logout() {
        sessionManager.clearSession()
        Log.d(TAG, "User logged out — session cleared")
    }

    // ── Private Helper Functions ──

    private fun getAppVersion(): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    private fun getAppVersionCode(): Int {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode
            }
        } catch (e: Exception) {
            1
        }
    }

    private fun getScreenDensity(): String {
        return try {
            val metrics = context.resources.displayMetrics
            val dpi = metrics.densityDpi
            val bucket = when {
                dpi <= DisplayMetrics.DENSITY_LOW -> "ldpi"
                dpi <= DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
                dpi <= DisplayMetrics.DENSITY_HIGH -> "hdpi"
                dpi <= DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
                dpi <= DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
                else -> "xxxhdpi"
            }
            "${dpi}dpi ($bucket)"
        } catch (e: Exception) {
            "unknown"
        }
    }

    private fun isRunningOnEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic")
                || Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }
}
