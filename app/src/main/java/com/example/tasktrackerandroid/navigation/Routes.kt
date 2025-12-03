package com.example.tasktrackerandroid.navigation

/**
 * Centralized navigation routes for the app.
 */
object Routes {

    // Authentication
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Tasks
    const val TASK_LIST = "task_list"
    const val TASK_EDIT = "task_edit/{taskId}"

    fun taskEditRoute(taskId: Int): String = "task_edit/$taskId"
}
