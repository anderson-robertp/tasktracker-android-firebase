package com.example.tasktrackerandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktrackerandroid.services.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    init {
        _state.value = _state.value.copy(
            isLoggedIn = authRepo.currentUser != null
        )
    }

    fun login(email: String, password: String) {
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
