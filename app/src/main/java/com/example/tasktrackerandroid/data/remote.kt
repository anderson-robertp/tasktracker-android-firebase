package com.example.tasktrackerandroid.data

import com.google.firebase.firestore.FirebaseFirestore
import com.example.tasktrackerandroid.data.model.Task
import kotlinx.coroutines.tasks.await

class FirebaseTaskServiceOld {

    private val db = FirebaseFirestore.getInstance()
    private val tasksRef = db.collection("tasks")

    suspend fun uploadTask(task: Task) {
        tasksRef.document(task.id.toString()).set(task).await()
    }

    suspend fun deleteTask(id: String) {
        tasksRef.document(id.toString()).delete().await()
    }

    suspend fun getAllTasks(): List<Task> {
        return tasksRef.get().await().toObjects(Task::class.java)
    }

    suspend fun updateTask(task: Task) {
        tasksRef.document(task.id.toString()).set(task).await()
    }
}
