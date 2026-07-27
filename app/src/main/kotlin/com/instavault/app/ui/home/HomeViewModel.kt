package com.instavault.app.ui.home

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
 * HomeViewModel — Hydrates HomeScreen with Real Encrypted User Profile Data
 *
 * Reads cached session & profile from AuthRepository (EncryptedSharedPreferences).
 * Triggers background telemetry sync on initialization.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Loads the cached UserProfile from encrypted storage and triggers background telemetry.
     */
    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val profile = authRepository.getCachedProfile()
            _userProfile.value = profile
            _isLoading.value = false

            // Fire-and-forget background telemetry sync (Step 5 of Auth Flow)
            authRepository.sendTelemetry()
        }
    }

    /**
     * Refresh user profile from local cache or trigger sync.
     */
    fun refreshProfile() {
        loadUserProfile()
    }
}
