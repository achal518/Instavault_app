package com.instavault.app.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.instavault.app.data.remote.dto.UserProfile
import com.instavault.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Login State — matches existing LoginScreen.kt UI bindings.
 * IDLE, LOADING, SUCCESS, ERROR are consumed by the Compose UI layer.
 */
enum class LoginState { IDLE, LOADING, SUCCESS, ERROR }

/**
 * LoginViewModel — Rewritten for Real Authentication
 *
 * Replaces the old mock delay + hardcoded accounts with real
 * AuthRepository-powered server authentication flow.
 *
 * Manages:
 *   - 5-digit Vault ID input state
 *   - Login state machine (IDLE → LOADING → SUCCESS/ERROR)
 *   - Error messages for user display
 *   - Authenticated user profile data
 *   - Background telemetry trigger after login
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    // ── Input State ──
    private val _digits = MutableStateFlow(List(5) { "" })
    val digits: StateFlow<List<String>> = _digits.asStateFlow()

    // ── Login State Machine ──
    private val _loginState = MutableStateFlow(LoginState.IDLE)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // ── User Display Data ──
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    // ── Error Message for UI ──
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ── Loading Progress Message for UI ──
    private val _loadingMessage = MutableStateFlow("Connecting...")
    val loadingMessage: StateFlow<String> = _loadingMessage.asStateFlow()

    // ── Authenticated User Profile (for Dashboard navigation) ──
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    /**
     * Handle digit input change. Resets error state when user starts re-typing.
     */
    fun onDigitChange(index: Int, value: String) {
        if (!value.matches(Regex("^\\d?$"))) return
        val currentDigits = _digits.value.toMutableList()
        currentDigits[index] = value
        _digits.value = currentDigits
        if (_loginState.value == LoginState.ERROR) {
            _loginState.value = LoginState.IDLE
            _errorMessage.value = null
        }
    }

    /**
     * Fill demo ID for quick testing. Preserves backward compatibility.
     */
    fun onFillDemo(id: String) {
        val d = id.replace("VLT-", "").map { it.toString() }
        if (d.size == 5) {
            _digits.value = d
            _loginState.value = LoginState.IDLE
            _errorMessage.value = null
        }
    }

    /**
     * Handle pasted Vault ID text.
     */
    fun onPaste(pasted: String) {
        val d = pasted.filter { it.isDigit() }.map { it.toString() }.take(5)
        if (d.size == 5) {
            _digits.value = d
            _loginState.value = LoginState.IDLE
            _errorMessage.value = null
        }
    }

    /**
     * Primary Login Action — Called when user taps "Connect" button.
     *
     * Orchestrates the full authentication flow via AuthRepository:
     *   Step 1: Verify Vault ID → get nonce
     *   Step 2: Request Play Integrity token
     *   Step 3: Verify integrity on server → get session + profile
     *   Step 4: Save session locally (encrypted)
     *   Step 5: Send background telemetry
     */
    fun onConnect() {
        val filled = _digits.value.all { it.isNotEmpty() }
        if (!filled) return
        val vaultId = "VLT-${_digits.value.joinToString("")}"

        _loginState.value = LoginState.LOADING
        _loadingMessage.value = "Verifying Vault ID..."
        _errorMessage.value = null

        viewModelScope.launch {
            // Execute the full login flow
            val result = authRepository.performFullLogin(vaultId)

            if (result.isSuccess) {
                val profile = result.getOrNull()
                _userProfile.value = profile
                _userName.value = profile?.firstName ?: "Vault Member"
                _loginState.value = LoginState.SUCCESS

                // Step 5: Fire-and-forget background telemetry
                launch {
                    authRepository.sendTelemetry()
                }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Login failed. Please try again."
                _errorMessage.value = error
                _loginState.value = LoginState.ERROR
            }
        }
    }

    /**
     * Check if user has an existing valid session (for auto-login on app startup).
     * Returns true if a cached session exists and navigates directly to Dashboard.
     */
    fun checkExistingSession(): Boolean {
        if (authRepository.isLoggedIn()) {
            val cachedProfile = authRepository.getCachedProfile()
            if (cachedProfile != null) {
                _userProfile.value = cachedProfile
                _userName.value = cachedProfile.firstName
                return true
            }
        }
        return false
    }

    /**
     * Logout — Clears encrypted session and resets all state.
     */
    fun logout() {
        authRepository.logout()
        reset()
    }

    /**
     * Reset all state to initial values.
     */
    fun reset() {
        _digits.value = List(5) { "" }
        _loginState.value = LoginState.IDLE
        _userName.value = null
        _errorMessage.value = null
        _loadingMessage.value = "Connecting..."
        _userProfile.value = null
    }
}
