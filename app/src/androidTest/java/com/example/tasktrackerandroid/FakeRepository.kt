package com.example.tasktrackerandroid

import com.example.tasktrackerandroid.data.TaskRepository
import com.example.tasktrackerandroid.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import java.util.concurrent.Flow

class FakeTaskRepository : TaskRepository {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun tasks(): Flow<List<Task>> = _tasks
    override fun observeTasks(): kotlinx.coroutines.flow.Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun startSync(scope: CoroutineScope) {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(task: Task) {
        _tasks.value = _tasks.value + task.copy(id = UUID.randomUUID().toString())
    }

    override suspend fun editTask(taskId: String, newTitle: String?) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(title = newTitle ?: it.title)
            else it
        }
    }

    override suspend fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
    }

    override suspend fun toggleTaskComplete(taskId: String, isCompleted: Boolean) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(isCompleted = isCompleted)
            else it
        }
    }
}
