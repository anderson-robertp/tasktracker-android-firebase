package com.example.tasktrackerandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandroid.data.model.Task
import com.example.tasktrackerandroid.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TaskScreen(viewModel: TaskViewModel)
fun TaskScreen(
    viewModel: TaskViewModel,
    editTask: (int: Int) -> Unit,
    deleteTask: (int: Int) -> Unit,
    modifier: Modifier = Modifier
    ) {
    val tasks by viewModel.tasks.collectAsState()
    var newTaskTitle by remember { mutableStateOf("") }

    val onAddTaskClick: () -> Unit = {
        if (newTaskTitle.isNotEmpty()) {
            viewModel.addTask(newTaskTitle)
            newTaskTitle = "" // Reset the state after adding
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    // Build the Task display screen
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Tracker") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskClick
            ) {
                Text("Add Task")
            }
        }
    ) { padding ->
        Column( // Column for the main content
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) { // Row for input and button
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("New Task") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
                // LazyColumn to display tasks
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskComplete(task.id) },
                        onClickEdit = { editTask(task.id) },
                        onClickDelete = { deleteTask(task.id) }
                    )
                }
            }
        }
    }
}


//
@Composable
// function to display task
fun TaskItem(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        task.title?.let { Text(it) }
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { task.isCompleted = it }
        )
    }
}

// Private fun to complete task
private fun TaskViewModel.toggleCompleted(task: Task) {
    toggleTaskComplete(task.id)
}
