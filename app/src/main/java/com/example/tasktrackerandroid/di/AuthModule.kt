package com.example.tasktrackerandroid.di

import com.example.tasktrackerandroid.data.auth.AuthRepositoryInterface
import com.example.tasktrackerandroid.data.auth.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: FirebaseAuthRepository
    ): AuthRepositoryInterface
}
