package com.example.tasktrackerandroid.di

import com.example.tasktrackerandroid.data.FirebaseTaskService
import com.example.tasktrackerandroid.data.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTaskRepository(
        impl: FirebaseTaskService
    ): TaskRepository
}