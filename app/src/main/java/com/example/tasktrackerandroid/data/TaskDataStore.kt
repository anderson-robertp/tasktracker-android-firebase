package com.example.tasktrackerandroid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.tasktrackerandroid.TaskList
import com.example.tasktrackerandroid.TaskProto
import com.example.tasktrackerandroid.data.datastore.TaskListSerializer
import com.example.tasktrackerandroid.data.model.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A repository-like class responsible for abstracting the read/write operations
 * for tasks from a Protobuf DataStore. It handles the conversion between the
 * Protobuf-generated data classes (`TaskList`, `TaskProto`) and the app's
 * domain model class (`Task`).
 *
 * @param context The application context, needed to create the DataStore.
 */
class TaskDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    /**
     * The actual DataStore instance. It is created using a factory to specify
     * the serializer and the file where the data will be stored.
     */
    private val dataStore: DataStore<TaskList> = DataStoreFactory.create(
        // Serializer for the DataStore that converts between TaskList and TaskProto
        serializer = TaskListSerializer,
        // Factory to create the file where the data will be stored
        produceFile = { context.dataStoreFile("tasks.pb") }
    )

    /**
     * A public Flow that exposes the list of tasks from the DataStore.
     * This Flow emits a new list of `Task` objects whenever the underlying data changes.
     * It maps the raw Protobuf `TaskList` object to a more usable `List<Task>`.
     */
    val tasksFlow: Flow<List<Task>> =
        // Map the raw Protobuf TaskList to a more usable List<Task>
        dataStore.data.map { taskList: TaskList ->
            // Convert each TaskProto in the TaskList to a Task object
            taskList.tasksList.map {
                // Create and return a Task object from the TaskProto
                Task(
                    id = it.id,
                    title = it.title,
                    isCompleted = it.isCompleted
                )
            }
        }

    /**
     * A suspending function to save a complete list of tasks to the DataStore.
     * This function overwrites the existing list with the new one provided.
     *
     * @param tasks The new list of `Task` objects to be saved.
     */
    suspend fun saveTasks(tasks: List<Task>) {
        // Convert the list of Task objects to a list of TaskProto objects
        dataStore.updateData { current ->
            // Create a new TaskList.Builder with the current data
            val builder = current.toBuilder().clearTasks()
            // Add each Task to the new TaskList.Builder
            tasks.forEach { task ->
                val taskProto = TaskProto.newBuilder()
                    .setId(task.id)
                    .setTitle(task.title)
                    .setIsCompleted(task.isCompleted)
                    .build()
                // Add the TaskProto to the new TaskList.Builder
                builder.addTasks(taskProto)
            }
            // Build the new TaskList and return it
            builder.build()
        }
    }

    suspend fun clearTasks() {
        dataStore.updateData { current ->
            current.toBuilder().clearTasks().build()
        }
    }
}

