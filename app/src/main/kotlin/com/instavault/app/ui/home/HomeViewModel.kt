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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    init {
        loadCachedProfile()
        viewModelScope.launch {
            authRepository.sendTelemetry()
        }
    }

    fun loadCachedProfile() {
        _userProfile.value = authRepository.getCachedProfile()
    }
}
