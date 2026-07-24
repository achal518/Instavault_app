package com.instavault.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response body from POST /auth/verify-integrity
 *
 * Success: {
 *   session_token: "uuid...",
 *   user_profile: { vault_id, first_name, spark_balance, rank_tier, ... }
 * }
 * Error: { error: "Device integrity check failed", details: "..." }
 */
data class VerifyIntegrityResponse(
    @SerializedName("session_token")
    val sessionToken: String? = null,

    @SerializedName("user_profile")
    val userProfile: UserProfile? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("details")
    val details: String? = null
)

/**
 * Nested user profile object inside VerifyIntegrityResponse.
 * Matches exactly with the server's authController.ts output.
 */
data class UserProfile(
    @SerializedName("vault_id")
    val vaultId: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("spark_balance")
    val sparkBalance: Int,

    @SerializedName("rank_tier")
    val rankTier: String,

    @SerializedName("lifetime_sparks")
    val lifetimeSparks: Int,

    @SerializedName("total_orders")
    val totalOrders: Int,

    @SerializedName("total_views_recv")
    val totalViewsReceived: Int,

    @SerializedName("instagram_handle")
    val instagramHandle: String?,

    @SerializedName("referral_count")
    val referralCount: Int
)
