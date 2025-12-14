package com.example.tasktrackerandroid.viewmodel

// Imports
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.ui.window.isPopupLayout
import androidx.lifecycle.viewModelScope
import com.example.tasktrackerandroid.data.model.Task
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.tasktrackerandroid.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

sealed class TaskStatus {
    object Loading : TaskStatus()
    data class Success(val tasks: List<Task>) : TaskStatus()
    object Idle : TaskStatus()
    data class Error(val message: String) : TaskStatus()
}
/**
 * The ViewModel for the task tracker application.
 * It extends AndroidViewModel to get access to the application context, which is needed
 * for initializing the DataStore. It's responsible for managing the UI state for the
 * task list and handling all business logic related to tasks (adding, editing, deleting, etc.).
 *
 * @param app The application context, provided by the AndroidViewModel superclass.
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _taskStatus = MutableStateFlow<TaskStatus>(TaskStatus.Idle)
    val taskStatus: StateFlow<TaskStatus> = _taskStatus.asStateFlow()

    private var isObserving = false

    fun startObservingTasks() {
        if (isObserving) return
        isObserving = true
        observeTasks()
    }

    fun stopObservingTasks() {
        isObserving = false
    }

    private fun observeTasks() {
        viewModelScope.launch {
            repository.tasks().collect { tasks ->
                _tasks.value = tasks
            }
        }
    }


    fun addTask(title: String) {
        val newTask = Task(
            id = "",
            title = title,
            isCompleted = false
        )
        viewModelScope.launch {
            _taskStatus.value = TaskStatus.Loading
            try {
                repository.addTask(newTask)
                _taskStatus.value = TaskStatus.Success(tasks.value + newTask)
        } catch (e: Exception) {
                _taskStatus.value = TaskStatus.Error(e.message ?: "Unknown error")
            }
        }
        Log.d("TaskViewModel","Task added: $title")
    }

    fun editTask(taskId: String, newTitle: String?) {
        viewModelScope.launch {
            try{
                repository.editTask(taskId, newTitle)
            } catch (e: Exception) {
                _taskStatus.value = TaskStatus.Error(e.message ?: "Unknown error")
            }
        }
        Log.d("TaskViewModel","Task edited: $taskId")
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
                Log.d("TaskViewModel","Task deleted: $taskId")
            } catch (e: Exception) {
                _taskStatus.value = TaskStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleTaskComplete(taskId: String) {
        viewModelScope.launch {
            try{
                val task = _tasks.value.find { it.id == taskId }?: return@launch
                repository.toggleTaskComplete(taskId, !task.isCompleted)
            } catch (e: Exception) {
                _taskStatus.value = TaskStatus.Error(e.message ?: "Unknown error")
            }
        }
        Log.d("TaskViewModel","Task toggled: $taskId")
    }


    /**
     * Returns a Flow that emits a single task matching the given taskId.
     * This is more efficient than observing the entire list on the UI when only one task is needed.
     */
    fun getTask(taskId: String): Flow<Task?> {
        return tasks.map { tasks ->
            tasks.find { it.id == taskId }
        }
    }

    fun resetStatus() {
        _taskStatus.value = TaskStatus.Idle
    }
}
