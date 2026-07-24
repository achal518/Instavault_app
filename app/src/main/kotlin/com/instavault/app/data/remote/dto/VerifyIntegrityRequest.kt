package com.instavault.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /auth/verify-integrity
 */
data class VerifyIntegrityRequest(
    @SerializedName("vault_id")
    val vaultId: String,

    @SerializedName("integrity_token")
    val integrityToken: String
)
