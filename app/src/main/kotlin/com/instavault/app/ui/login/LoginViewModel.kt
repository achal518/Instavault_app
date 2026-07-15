package com.instavault.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class LoginState { IDLE, LOADING, SUCCESS, ERROR }

class LoginViewModel : ViewModel() {
    private val _digits = MutableStateFlow(List(5) { "" })
    val digits: StateFlow<List<String>> = _digits.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState.IDLE)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _userName = MutableStateFlow<String?>("Demo User")
    val userName: StateFlow<String?> = _userName.asStateFlow()

    fun onDigitChange(index: Int, value: String) {
        if (!value.matches(Regex("^\\d?\$"))) return
        val currentDigits = _digits.value.toMutableList()
        currentDigits[index] = value
        _digits.value = currentDigits
        if (_loginState.value == LoginState.ERROR) {
            _loginState.value = LoginState.IDLE
        }
    }
    
    fun onFillDemo(id: String) {
        val d = id.replace("VLT-", "").map { it.toString() }
        if (d.size == 5) {
            _digits.value = d
            _loginState.value = LoginState.IDLE
        }
    }

    fun onPaste(pasted: String) {
        val d = pasted.filter { it.isDigit() }.map { it.toString() }.take(5)
        if (d.size == 5) {
            _digits.value = d
            _loginState.value = LoginState.IDLE
        }
    }

    fun onConnect() {
        val filled = _digits.value.all { it.isNotEmpty() }
        if (!filled) return
        val vaultId = "VLT-${_digits.value.joinToString("")}"
        
        _loginState.value = LoginState.LOADING
        
        // TODO: Wire real authentication/API logic here
        // domain/login and data/remote are explicitly out of scope.
        // This is a stub action.
        viewModelScope.launch {
            delay(1800)
            if (vaultId in listOf("VLT-00001", "VLT-00847", "VLT-01234")) {
                val mockName = when (vaultId) {
                    "VLT-00001" -> "Rahul"
                    "VLT-00847" -> "Priya"
                    else -> "Arjun"
                }
                _userName.value = mockName
                _loginState.value = LoginState.SUCCESS
            } else {
                _loginState.value = LoginState.ERROR
            }
        }
    }
    
    fun reset() {
        _digits.value = List(5) { "" }
        _loginState.value = LoginState.IDLE
        _userName.value = null
    }
}
