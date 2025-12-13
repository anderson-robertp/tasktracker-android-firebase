package com.example.tasktrackerandroid.data

import com.example.tasktrackerandroid.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun tasks(): Flow<List<Task>>

    fun observeTasks(): Flow<List<Task>>


    fun startSync(scope: CoroutineScope)

    suspend fun addTask(task: Task) : Result<Unit>


    suspend fun editTask(taskId: String, newTitle: String?)

    suspend fun deleteTask(taskId: String)

    suspend fun toggleTaskComplete(taskId: String, isCompleted: Boolean)

}
