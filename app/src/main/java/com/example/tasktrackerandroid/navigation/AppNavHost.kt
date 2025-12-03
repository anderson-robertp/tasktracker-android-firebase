package com.example.tasktrackerandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tasktrackerandroid.viewmodel.TaskViewModel
import com.example.tasktrackerandroid.ui.screens.TaskScreen
import com.example.tasktrackerandroid.ui.screens.TaskEditScreen
import com.example.tasktrackerandroid.ui.screens.TaskListScreen
import com.example.tasktrackerandroid.ui.screens.LoginScreen
import com.example.tasktrackerandroid.ui.screens.RegisterScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tasktrackerandroid.navigation.Routes
import com.example.tasktrackerandroid.viewmodel.AuthViewModel


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
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.TASK_LIST else Routes.LOGIN
    ) {
        // ---------- AUTH ----------
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.TASK_LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.TASK_LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // ---------- TASK LIST ----------
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                onAddTask = { navController.navigate(Routes.taskEditRoute(-1)) },
                onTaskClick = { taskId ->
                    navController.navigate(Routes.taskEditRoute(taskId))
                }
            )
        }

        // ---------- TASK EDIT ----------
        composable(
            route = Routes.TASK_EDIT,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments!!.getInt("taskId")

            TaskEditScreen(
                taskId = taskId,
                onSave = {
                    navController.popBackStack()
                }
            )
        }
    }
}

