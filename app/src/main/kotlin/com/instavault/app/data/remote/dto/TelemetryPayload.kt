package com.instavault.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /telemetry/log-session
 * Deep device metrics sent by the background worker after login.
 * Matches server's telemetryController.ts TelemetryPayload interface.
 */
data class TelemetryPayload(
    @SerializedName("device_model")
    val deviceModel: String,

    @SerializedName("device_brand")
    val deviceBrand: String,

    @SerializedName("os_version")
    val osVersion: String,

    @SerializedName("app_version")
    val appVersion: String,

    @SerializedName("build_number")
    val buildNumber: Int,

    @SerializedName("device_locale")
    val deviceLocale: String,

    @SerializedName("screen_density")
    val screenDensity: String,

    @SerializedName("network_type")
    val networkType: String,

    @SerializedName("is_emulator")
    val isEmulator: Boolean,

    @SerializedName("is_rooted")
    val isRooted: Boolean,

    @SerializedName("client_timestamp")
    val clientTimestamp: String
)
