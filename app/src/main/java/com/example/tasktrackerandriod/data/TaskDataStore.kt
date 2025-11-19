package com.example.tasktrackerandriod.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.tasktrackerandriod.TaskList
import com.example.tasktrackerandriod.TaskProto
import com.example.tasktrackerandriod.data.datastore.TaskListSerializer
import com.example.tasktrackerandriod.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class TaskDataStore(private val context: Context) {

    private val dataStore: DataStore<TaskList> = DataStoreFactory.create(
        serializer = TaskListSerializer,
        produceFile = { context.dataStoreFile("tasks.pb") }
    )

    val tasksFlow: Flow<List<Task>> =
        dataStore.data.map { taskList: TaskList ->
            taskList.tasksList.map { taskProto: TaskProto ->
                Task(
                    id = taskProto.id,
                    title = taskProto.title,
                    isCompleted = taskProto.isCompleted
                )
            }
        }

    suspend fun saveTasks(tasks: List<Task>) {
        dataStore.updateData { current ->
            val builder = current.toBuilder().clearTasks()

            tasks.forEach { task ->
                val taskProto = TaskProto.newBuilder()
                    .setId(task.id)
                    .setTitle(task.title)
                    .setIsCompleted(task.isCompleted)
                    .build()
                builder.addTasks(taskProto)
            }
            builder.build()
        }
    }
}

