package com.example.tasktrackerandroid.data.auth

interface AuthRepositoryInterface {
    val currentUser: Any?
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    fun logout()
}