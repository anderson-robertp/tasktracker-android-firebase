package com.example.tasktrackerandriod.viewmodel

// Imports
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.tasktrackerandriod.data.TaskDataStore
import com.example.tasktrackerandriod.data.model.Task
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.tasktrackerandriod.data.remote.FirebaseTaskService



/**
 * The ViewModel for the task tracker application.
 * It extends AndroidViewModel to get access to the application context, which is needed
 * for initializing the DataStore. It's responsible for managing the UI state for the
 * task list and handling all business logic related to tasks (adding, editing, deleting, etc.).
 *
 * @param app The application context, provided by the AndroidViewModel superclass.
 */
class TaskViewModel(app: Application) : AndroidViewModel(app) {
    // Private instance of the Datastore repository
    private val dataStore = TaskDataStore(app)
    // Mutable state flow for the list of tasks
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    // Public immutable state flow for the list of tasks
    val tasks: StateFlow<List<Task>> = _tasks
    // Counter for generating unique task IDs
    private var nextId: Int = 1
    // Firebase
    private val remote = FirebaseTaskService()

    /**
     * The init block is executed when the ViewModel is first created.
     * It launches a coroutine to collect the flow of tasks from the DataStore
     * and update the local UI state.
     */
    init {
        viewModelScope.launch {
            dataStore.tasksFlow.collect { loaded ->
                _tasks.value = loaded
                nextId = loaded.maxOfOrNull { it.id }?.plus(1) ?: 1
            }
            val tasks = remote.getAllTasks()
            if (tasks.isNotEmpty()) {
                updateTasks(tasks)
            }
        }

    }

    /**
     * A private helper function to centralize the logic for updating the task list.
     * It updates both the local UI state and persists the changes to the DataStore.
     *
     * @param tasks The new, updated list of tasks.
     */
    private fun updateTasks(tasks: List<Task>) {
        _tasks.value = tasks
        viewModelScope.launch {
            dataStore.saveTasks(tasks)
        }
    }

    // Public functions
    /**
     * Adds a new task to the list.
     *
     * @param title The title of the new task.
     */
    fun addTask(title: String) {
        val newId = (tasks.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(id = newId, title = title)
        updateTasks(tasks.value + newTask)

        viewModelScope.launch {
            remote.uploadTask(newTask)
        }
    }

    /**
     * Toggles the completion status of a specific task.
     *
     * @param id The unique ID of the task to toggle.
     */
    fun toggleTaskComplete(id: Int) {
        val updated = tasks.value.map {
            if (it.id == id) {
                val updatedTask = it.copy(isCompleted = !it.isCompleted)
                viewModelScope.launch {
                    remote.updateTask(updatedTask)
                }
                updatedTask
            }
            else it
        }
        updateTasks(updated)
    }

    /**
     * Edits the title of a specific task.
     *
     * @param id The unique ID of the task to edit.
     * @param newTitle The new title for the task.
     */
    fun editTask(id: Int, newTitle: String) {
        // FIX 1.1: Use a different variable name ('updatedTasks')
        val updatedTasks = tasks.value.map {
            if (it.id == id) {
                val updatedTask = it.copy(title = newTitle)
                viewModelScope.launch {
                    remote.updateTask(updatedTask)
                }
                updatedTask
            }
            else it
        }
        // FIX 1.2: Pass the correct variable to 'saveTasks'
        updateTasks(updatedTasks)
    }

    /**
     * Deletes a task from the list.
     *
     * @param id The unique ID of the task to delete.
     */
    fun deleteTask(id: Int) {
        val remainingTasks = tasks.value.filterNot { it.id == id }
        updateTasks(remainingTasks)
        viewModelScope.launch {
            remote.deleteTask(id)
        }
    }
}