package com.example.tasktrackerandroid.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AuthService(
    private val auth: FirebaseAuth
) {

    val currentUser get() = auth.currentUser

    // Sign up new user
    suspend fun register(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { cont.resume(Result.success(it.user!!)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    // Login existing user
    suspend fun login(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { cont.resume(Result.success(it.user!!)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    // Logout
    fun logout() {
        auth.signOut()
    }
}
