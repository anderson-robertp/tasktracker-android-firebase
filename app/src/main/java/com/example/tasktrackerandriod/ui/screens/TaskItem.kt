package com.example.tasktrackerandriod.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandriod.data.model.Task

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClickEdit() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (task.isCompleted) "✔ ${task.title}" else task.title,
                modifier = Modifier.clickable { onClickEdit() }
            )

            Row {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() }
                )
                IconButton(onClick = onClickDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete task")
                }
            }
        }
    }
}
