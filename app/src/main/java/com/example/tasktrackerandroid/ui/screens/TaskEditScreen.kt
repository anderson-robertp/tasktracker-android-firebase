package com.example.tasktrackerandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandroid.viewmodel.TaskViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavController

/**
 * A composable screen for editing or deleting a specific task.
 * It displays the task's details and provides input fields to modify them.
 *
 * @param taskId The unique ID of the task to be edited.
 * @param navController The navigation controller, passed down to manage UI-driven navigation events.
 * @param viewModel The ViewModel instance providing data and business logic for tasks.
 * @param onNavigateBack A callback function to be invoked when the user wants to go back to the previous screen.
 * @param modifier A Modifier to be applied to the root composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    taskId: String,
    navController: NavController,
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Collect the list of tasks as state from the ViewModel.
    // `collectAsState` ensures that the Composable recomposes whenever the task list changes.
    // Then, find the specific task that matches the provided `taskId`.
    val task by viewModel.getTask(taskId).collectAsState(initial = null)

    // If the task is not found (e.g., it was deleted), display a message and stop rendering.
    if (task == null) {
        Text("Task not found.")
        return
    }
    // Create a mutable state for the task details.
    // These states are initialized with the task's current values and can be updated by the UI.
    var updatedTitle by remember { mutableStateOf(task!!.title) }
    var updatedCompleted by remember { mutableStateOf(task!!.isCompleted) }

    // Create a scaffold with a top bar and content
    Scaffold(
        topBar = {
            // Top app bar with a title and a back button
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Main content column with padding
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Text field for editing the task title
            updatedTitle?.let { it1 ->
                OutlinedTextField(
                    value = it1,
                    onValueChange = { updatedTitle = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            // Checkbox to mark the task as completed
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = updatedCompleted,
                    onCheckedChange = { updatedCompleted = it }
                )
                Text("Completed")
            }

            Spacer(modifier = Modifier.height(32.dp))
            // Button to save the changes and navigate back to the previous screen
            Button(
                onClick = {
                    viewModel.editTask(taskId, updatedTitle)
                    if (updatedCompleted != task!!.isCompleted)
                        viewModel.toggleTaskComplete(taskId)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Button to delete the task and navigate back to the previous screen
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                onClick = {
                    viewModel.deleteTask(taskId)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Task")
            }
        }
    }
}

