package com.instavault.app.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.instavault.app.data.remote.dto.UserProfile

/**
 * SessionManager — Secure Encrypted Session Storage
 *
 * Manages the user's authenticated session using Android's EncryptedSharedPreferences,
 * which encrypts all keys and values with AES-256-GCM backed by the hardware Android Keystore.
 *
 * Stores:
 *   - Session Token (UUID from server)
 *   - User Profile (JSON serialized)
 *
 * Session persists across app restarts until:
 *   - User explicitly logs out (clearSession)
 *   - App data is cleared
 *   - App is uninstalled
 */
class SessionManager(context: Context) {

    companion object {
        private const val PREFS_FILE_NAME = "instavault_secure_session"
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_USER_PROFILE = "user_profile_json"
        private const val KEY_VAULT_ID = "vault_id"
    }

    private val gson = Gson()

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Saves the full session data after successful authentication.
     * Called once after /auth/verify-integrity returns session_token + user_profile.
     */
    fun saveSession(sessionToken: String, vaultId: String, userProfile: UserProfile) {
        val profileJson = gson.toJson(userProfile)
        prefs.edit()
            .putString(KEY_SESSION_TOKEN, sessionToken)
            .putString(KEY_VAULT_ID, vaultId)
            .putString(KEY_USER_PROFILE, profileJson)
            .apply()
    }

    /**
     * Returns the stored session token, or null if no session exists.
     */
    fun getSessionToken(): String? {
        return prefs.getString(KEY_SESSION_TOKEN, null)
    }

    /**
     * Returns the stored Vault ID, or null if no session exists.
     */
    fun getVaultId(): String? {
        return prefs.getString(KEY_VAULT_ID, null)
    }

    /**
     * Returns the stored UserProfile object deserialized from JSON, or null.
     */
    fun getUserProfile(): UserProfile? {
        val json = prefs.getString(KEY_USER_PROFILE, null) ?: return null
        return try {
            gson.fromJson(json, UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Quick check: Does a valid (non-null) session exist?
     * Used on app startup to decide: show Login or skip to Dashboard.
     */
    fun hasValidSession(): Boolean {
        return !getSessionToken().isNullOrEmpty() && getUserProfile() != null
    }

    /**
     * Clears all session data.
     * Called on logout or when server returns 401 (session expired/invalid).
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
