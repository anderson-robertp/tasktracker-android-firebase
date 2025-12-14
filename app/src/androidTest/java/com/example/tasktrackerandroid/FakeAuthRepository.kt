package com.example.tasktrackerandroid

import com.example.tasktrackerandroid.data.auth.AuthRepositoryInterface

class FakeAuthRepository() : AuthRepositoryInterface {

    override val currentUser: Any
        get() {
            TODO()
        }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }


}