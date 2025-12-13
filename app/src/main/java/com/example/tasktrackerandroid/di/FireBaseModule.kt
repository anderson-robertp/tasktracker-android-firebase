package com.example.tasktrackerandroid.di

import com.example.tasktrackerandroid.data.FirebaseTaskService
import com.example.tasktrackerandroid.data.TaskDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseTaskService(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        dataStore: TaskDataStore
    ): FirebaseTaskService =
        FirebaseTaskService(firestore, auth, dataStore)
}
