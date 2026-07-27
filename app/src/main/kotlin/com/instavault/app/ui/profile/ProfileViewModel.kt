package com.instavault.app.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.instavault.app.data.remote.dto.UserProfile
import com.instavault.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    private val _userProfile = MutableStateFlow<UserProfile?>(authRepository.getCachedProfile())
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    fun logout() {
        authRepository.logout()
        _userProfile.value = null
    }
}
