package com.example.tasktrackerandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tasktrackerandroid.viewmodel.TaskViewModel
import com.example.tasktrackerandroid.ui.screens.TaskScreen
import com.example.tasktrackerandroid.ui.screens.TaskEditScreen

/**
 * The main navigation host for the application.
 * This composable is responsible for defining the navigation graph, which includes
 * all possible screens (destinations) and the routes to get to them.
 *
 * @param navController The controller that manages app navigation. It is hoisted
 * from MainActivity to be used here for navigating between screens.
 * @param viewModel An instance of TaskViewModel, passed down to the screens
 * that need to interact with business logic and data.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    // Define the navigation graph using NavHost.
    NavHost(
        navController = navController,
        // Set the initial destination to the task list screen.
        startDestination = Routes.TASK_LIST
    ) {
        // Define the route for the task list screen.
        composable(Routes.TASK_LIST) {
            // Call the TaskScreen composable, passing the necessary parameters.
            TaskScreen(
                viewModel = viewModel,
                // Define the actions that can be performed on tasks.
                // TaskScreen will call these actions when the user interacts with the tasks.
                editTask = { taskId ->
                    // Navigate to the task edit screen with the provided taskId.
                    navController.navigate("task_edit/$taskId")
                },
                // Delete a task when the user requests it.
                deleteTask = { taskId ->
                    viewModel.deleteTask(taskId)
                }
            )
        }
        // Define the route for the task edit screen.
        // This screen is accessed when the user wants to edit an existing task.
        composable("task_edit/{taskId}") { backStackEntry ->
            // Retrieve the taskID from the navigation arguments.
            // This is used to identify the task to be edited.
            val id = backStackEntry.arguments?.getString("taskId")?.toInt() ?: 0
            // Call the TaskEditScreen composable, passing the necessary parameters.
            TaskEditScreen(
                viewModel = viewModel,
                taskId = id,
                // Define the actions that can be performed on tasks.
                // TaskEditScreen will call these actions when the user interacts with the tasks.
                // navcontroller is used to navigate back to the task list screen.
                navController = navController,
                // A callback function that is called when the user clicks the back button.
                // This will navigate back to the task list screen.
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

