package com.instavault.app.security

/**
 * A highly robust sealed class that cleanly encapsulates the state of a Play Integrity token request.
 * This allows the UI or calling functions to easily handle the result using exhaustive `when` statements.
 */
sealed class AttestationResult {
    /**
     * Initial state while the OS is fetching the token.
     */
    object Loading : AttestationResult()

    /**
     * Successful state containing the signed JWT token.
     * @param token The encrypted JWT token returned by Play Services.
     */
    data class Success(val token: String) : AttestationResult()

    /**
     * Error state containing specific failure details.
     * @param message A developer/user friendly error message.
     * @param exception The underlying exception (usually an IntegrityServiceException).
     */
    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : AttestationResult()
}
