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
    taskViewModel: TaskViewModel,
    authViewModel: AuthViewModel,
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.TASK_LIST else Routes.LOGIN
    ) {
        // Login
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.TASK_LIST) {}
                }
            )
        }

        // Register
        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.TASK_LIST) {}
                },
                onBackToLogin = {
                    navController.popBackStack()
                }

            )
        }

        // Task List
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                navController = navController,
                viewModel = taskViewModel,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.TASK_LIST) { inclusive = true }
                    }
                }
            )
        }

        // Task Edit
        composable(
            route = Routes.TASK_EDIT,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ){ backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                requireNotNull(taskId) { "Task ID is required" }
                TaskEditScreen(
                    navController = navController,
                    viewModel = taskViewModel,
                    taskId = taskId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
        }
    }
}

