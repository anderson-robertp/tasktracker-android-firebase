package com.example.tasktrackerandriod.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandriod.viewmodel.TaskViewModel
import com.example.tasktrackerandriod.ui.screens.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {

    var newTaskTitle by remember { mutableStateOf("") }
    val onAddTaskClick: () -> Unit = {
        if (newTaskTitle.isNotEmpty()) {
            viewModel.addTask(newTaskTitle)
            newTaskTitle = "" // Reset the state after adding
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Tracker") })
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

            Spacer(modifier = Modifier.height(16.dp))

            viewModel.tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTaskComplete(task.id) },
                    onClickEdit = { /* Nav later */ }
                )
            }
        }
    }
}