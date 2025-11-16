package com.example.tasktrackerandriod.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.tasktrackerandriod.data.model.Task

class TaskViewModel : ViewModel() {

    private var nextId = 5
    private val _tasks = mutableStateListOf<Task>()

    val tasks: List<Task> = _tasks

    init {
        // Add sample data
        _tasks.addAll(
            listOf(
                Task(1,"Buy groceries"),
                Task(2,"Finish Kotlin project"),
                Task(3,"Read a book", isCompleted = true),
                Task(4,"Walk the dog")
            )
        )
    }

    fun addTask(title: String) {
        if (title.isNotBlank()) {
            _tasks.add(Task(nextId++, title))
        }
    }

    fun toggleTaskComplete(id: Int) {
        val task = tasks.find { it.id == id }
        task?.isCompleted = !(task.isCompleted)
    }

    /*fun updateTask(id: Int, newTitle: String) {
        val task = tasks.find { it.id == id }
        if (task != null) {
            task.title = newTitle
        }
    }*/

    fun editTask(id: Int, newTitle: String) {
        val task = tasks.find { it.id == id }
        if (task != null) {
            task.title = newTitle
        }
    }

    fun deleteTask(id: Int) {
        _tasks.removeAll { it.id == id }
    }
}