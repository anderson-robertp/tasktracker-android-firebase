package com.example.tasktrackerandroid.navigation

/**
 * A singleton object that centralizes all navigation routes used in the application.
 *
 * Using a dedicated object for routes is a best practice that helps prevent typos
 * and makes the navigation graph easier to manage and refactor. Instead of
 * hardcoding route strings like "task_list" throughout the app, we can use
 * the type-safe constants defined here (e.g., `Routes.TASK_LIST`).
 */
object Routes {
    /**
     * The route for the main screen that displays the list of all tasks.
     * This is typically the start destination of the navigation graph.
     */
    const val TASK_LIST = "task_list"
    /**
     * The base route for the screen used to edit a specific task.
     * It includes a placeholder `{taskId}` for a dynamic argument, which allows
     * the navigation action to specify which task needs to be edited.
     * For example, navigating to "task_edit/5" will open the edit screen for the task with ID 5.
     */
    const val TASK_EDIT = "task_edit/{taskId}"
}