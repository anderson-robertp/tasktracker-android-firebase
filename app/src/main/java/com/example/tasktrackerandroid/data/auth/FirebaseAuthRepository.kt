package com.example.tasktrackerandroid.data.auth

// Imports
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of the [AuthRepositoryInterface] using Firebase Authentication.
 * @param auth The Firebase Authentication instance.
 * @property currentUser The currently logged-in user, if any.
 * @property login Logs in a user with the given email and password.
 * @property register Registers a new user with the given email and password.
 * @property logout Logs out the currently logged-in user.
 */
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepositoryInterface {

    // Properties
    override val currentUser
        get() = auth.currentUser

    // Methods
    /**
     * Logs in a user with the given email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] indicating the success or failure of the login operation.
     */
    override suspend fun login(email: String, password: String): Result<Unit> =
        // Use Firebase's signInWithEmailAndPassword method to log in the user
        runCatching {
            auth.signInWithEmailAndPassword(email, password).await()
        }

    /**
     * Registers a new user with the given email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] indicating the success or failure of the registration operation.
     */
    override suspend fun register(email: String, password: String): Result<Unit> =
        // Use Firebase's createUserWithEmailAndPassword method to register the user
        runCatching {
            auth.createUserWithEmailAndPassword(email, password).await()
        }

    /**
     * Logs out the currently logged-in user.
     * @return A [Result] indicating the success or failure of the logout operation.
     */
    override fun logout() {
        // Use Firebase's signOut method to log out the user
        auth.signOut()
    }
}