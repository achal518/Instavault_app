package com.instavault.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /auth/verify-vault-id
 */
data class VerifyVaultIdRequest(
    @SerializedName("vault_id")
    val vaultId: String
)
