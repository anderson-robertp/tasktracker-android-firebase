package com.example.tasktrackerandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktrackerandroid.data.model.Task

/**
 * A composable that displays a single task item in a list.
 * It shows the task's title and provides controls for toggling its completion status,
 * editing, and deleting the task.
 *
 * @param task The `Task` object containing the data to display.
 * @param onToggle A callback function to be invoked when the user toggles the checkbox.
 * @param onClickEdit A callback function to be invoked when the user clicks on the item to edit it.
 * @param onClickDelete A callback function to be invoked when the user clicks the delete icon.
 */
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
) {
    // A card that represents a single task item.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClickEdit() }
    ) {
        // A row that displays the task title and a checkbox for completion.
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // A text that displays the task title.
            if (task.isCompleted) "✔ ${task.title}" else task.title?.let {
                Text(
                    text = it,
                    modifier = Modifier.clickable { onClickEdit() }
                )
            }
            // A row that contains the delete icon and a checkbox for completion.
            Row {
                // A checkbox for completion.
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() }
                )
                // An icon for deleting the task.
                IconButton(onClick = onClickDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete task")
                }
            }
        }
    }
}
