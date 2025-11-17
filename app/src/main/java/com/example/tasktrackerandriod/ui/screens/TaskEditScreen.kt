package com.example.tasktrackerandriod.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandriod.viewmodel.TaskViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    taskId: Int,
    navController: NavController,
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Fetch the task details based on the taskId
    val task = viewModel.tasks.find { it.id == taskId }

    if (task == null) {
        Text("Task not found.")
        return
    }
    // Create a mutable state for the task details
    var updatedTitle by remember { mutableStateOf(task.title) }
    var updatedCompleted by remember { mutableStateOf(task.isCompleted) }

    // Create a scaffold with a top bar and content
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = updatedTitle,
                onValueChange = { updatedTitle = it },
                label = { Text("Task Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

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

            Button(
                onClick = {
                    viewModel.editTask(taskId, updatedTitle)
                    if (updatedCompleted != task.isCompleted)
                        viewModel.toggleTaskComplete(taskId)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                    viewModel.deleteTask(taskId)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Task")
            }
        }
    }
}
