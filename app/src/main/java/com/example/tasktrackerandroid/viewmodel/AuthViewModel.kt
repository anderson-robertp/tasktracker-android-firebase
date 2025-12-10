package com.example.tasktrackerandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktrackerandroid.services.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val isPasswordReset: Boolean = false,
    val generalError: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state


    init {
        _state.value = _state.value.copy(
            isLoggedIn = authRepo.currentUser != null
        )
    }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun login(email: String, password: String) {
        // Call Repository
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = authRepo.login(email, password)
            _state.update {
                if (result.isSuccess) {
                    it.copy(isLoading = false, isLoggedIn = true)
                } else {
                    it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun register(email: String, password: String) {
        val state = _state.value

        // Validate
        if (!state.email.isBlank()) {
            _state.update { it.copy(emailError = "Email cannot be empty") }
            return
        }
        if (!state.password.isBlank()) {
            _state.update { it.copy(passwordError = "Password cannot be empty") }
            return
        }
        if (state.password != state.confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = authRepo.register(email, password)
            _state.update {
                if (result.isSuccess) {
                    it.copy(isLoading = false, isLoggedIn = true)
                } else {
                    it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun logout() {
        authRepo.logout()
        _state.update { it.copy(isLoggedIn = false) }
    }
}
