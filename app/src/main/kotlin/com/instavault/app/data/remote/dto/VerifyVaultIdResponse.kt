package com.instavault.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response body from POST /auth/verify-vault-id
 *
 * Success: { valid: true, nonce: "base64url..." }
 * Error:   { valid: false, error: "Vault ID not found" }
 */
data class VerifyVaultIdResponse(
    @SerializedName("valid")
    val valid: Boolean,

    @SerializedName("nonce")
    val nonce: String? = null,

    @SerializedName("error")
    val error: String? = null
)
