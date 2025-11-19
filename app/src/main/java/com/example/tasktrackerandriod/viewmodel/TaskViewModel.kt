package com.example.tasktrackerandriod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktrackerandriod.data.TaskDataStore
import com.example.tasktrackerandriod.data.taskDataStore
import com.example.tasktrackerandriod.data.model.Task
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import com.example.tasktrackerandriod.data.protoTaskDataStore
import kotlinx.coroutines.flow.MutableStateFlow

class TaskViewModel(context: Context) : ViewModel() {
     private val dataStore: TaskDataStore = context.protoTaskDataStore

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>> = _tasks

    private var nextId: Int = 1

    init {
        viewModelScope.launch {
            dataStore.tasksFlow.collect { loaded ->
                _tasks.value = loaded
                nextId = loaded.maxOfOrNull { it.id }?.plus(1) ?: 1
            }

        }

    }

    private fun updateTasks(tasks: List<Task>) {
        _tasks.value = tasks
        viewModelScope.launch {
            dataStore.saveTasks(tasks)
        }
    }




    fun addTask(title: String) {
        val newId = (tasks.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(id = newId, title = title)
        updateTasks(tasks.value + newTask)
    }

    fun toggleTaskComplete(id: Int) {
        val updated = tasks.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted)
            else it
        }
        updateTasks(updated)
    }

    fun editTask(id: Int, newTitle: String) {
        // FIX 1.1: Use a different variable name ('updatedTasks')
        val updatedTasks = tasks.value.map {
            if (it.id == id) it.copy(title = newTitle)
            else it
        }
        // FIX 1.2: Pass the correct variable to 'saveTasks'
        updateTasks(updatedTasks)
    }

    fun deleteTask(id: Int) {
        val remainingTasks = tasks.value.filterNot { it.id == id }
        updateTasks(remainingTasks)
    }
}