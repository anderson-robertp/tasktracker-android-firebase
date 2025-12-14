package com.example.tasktrackerandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tasktrackerandroid.viewmodel.TaskStatus
import com.example.tasktrackerandroid.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    onLogout: () -> Unit
) {

    val tasks by viewModel.tasks.collectAsState()
    val taskStatus by viewModel.taskStatus.collectAsState()
    var newTaskTitle by remember { mutableStateOf("") }

    val onAddTaskClick: () -> Unit = {
        if (newTaskTitle.isNotEmpty()) {
            viewModel.addTask(newTaskTitle)
            newTaskTitle = "" // Reset the state after adding
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(taskStatus) {
        when (taskStatus) {
            is TaskStatus.Success -> {
                snackbarHostState.showSnackbar("Task added successfully")
                viewModel.resetStatus()
            }

            is TaskStatus.Error -> {
                val message = (taskStatus as TaskStatus.Error).message
                snackbarHostState.showSnackbar(message)
                viewModel.resetStatus()
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Task Tracker") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskClick
            ) {
                Text("Add Task")
            }
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("New Task") },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskComplete(task.id) },
                        onClickEdit = { navController.navigate("task_edit/${task.id}") },
                        onClickDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }
}