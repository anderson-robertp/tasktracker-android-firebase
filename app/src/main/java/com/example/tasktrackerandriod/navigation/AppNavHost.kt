package com.example.tasktrackerandriod.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tasktrackerandriod.viewmodel.TaskViewModel
import com.example.tasktrackerandriod.ui.screens.TaskScreen
import com.example.tasktrackerandriod.ui.screens.TaskEditScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "Routes.TASK_LIST"
    ) {
        composable(Routes.TASK_LIST) {
            TaskScreen(
                viewModel = viewModel,
                editTask = { taskId ->
                    navController.navigate("task_edit/$taskId")
                }
            )
        }

        composable("task_edit/{taskId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("taskId")?.toInt() ?: 0

            TaskEditScreen(
                viewModel = viewModel,
                taskId = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
