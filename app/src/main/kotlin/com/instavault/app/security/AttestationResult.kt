package com.instavault.app.security

// TODO: Token ka data class yahan aayega
data class AttestationResult(
    val token: String = "",
    val isValid: Boolean = false
)
