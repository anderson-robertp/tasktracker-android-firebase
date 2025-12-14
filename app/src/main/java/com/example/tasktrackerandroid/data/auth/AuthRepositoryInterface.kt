package com.example.tasktrackerandroid.data.auth

/**
 * Interface for the authentication repository.
 * Provides methods for logging in, registering, and logging out users.
 * @property currentUser The currently logged-in user, if any.
 * @property login Logs in a user with the given email and password.
 * @property register Registers a new user with the given email and password.
 * @property logout Logs out the currently logged-in user.
 */
interface AuthRepositoryInterface {
    // Properties
    val currentUser: Any?

    // Methods
    /**
     * Logs in a user with the given email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] indicating the success or failure of the login operation.
     */
    suspend fun login(email: String, password: String): Result<Unit>

    /**
     * Registers a new user with the given email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] indicating the success or failure of the registration operation.
     */
    suspend fun register(email: String, password: String): Result<Unit>

    /**
     * Logs out the currently logged-in user.
     * @return A [Result] indicating the success or failure of the logout operation.
     */
    fun logout()
}