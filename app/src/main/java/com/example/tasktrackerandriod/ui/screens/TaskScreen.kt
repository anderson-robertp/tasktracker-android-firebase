package com.example.tasktrackerandriod.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandriod.data.model.Task
import com.example.tasktrackerandriod.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel, modifier: Modifier) {
    val tasks = viewModel.tasks
    var newTaskTitle by remember { mutableStateOf("") }

    val onAddTaskClick: () -> Unit = {
        if (newTaskTitle.isNotEmpty()) {
            viewModel.addTask(newTaskTitle)
            newTaskTitle = "" // Reset the state after adding
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("New Task") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskComplete(task.id) },
                        onClickEdit = { onNavigateToEdit.editTask(task.id) },
                        onClickDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(task.title)
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { task.isCompleted = it }
        )
    }
}

private fun TaskViewModel.toggleCompleted(task: Task) {
    toggleTaskComplete(task.id)
}
