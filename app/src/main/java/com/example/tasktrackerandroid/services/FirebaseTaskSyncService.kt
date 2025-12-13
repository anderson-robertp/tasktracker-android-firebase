package com.example.tasktrackerandroid.services

import com.example.tasktrackerandroid.data.TaskDataStore
import com.example.tasktrackerandroid.data.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FirebaseTaskSyncService(
    private val dataStore: TaskDataStore
) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun startSync(scope: CoroutineScope) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("tasks")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val tasks = snapshot.documents.map { doc ->
                    Task(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        isCompleted = doc.getBoolean("isCompleted") ?: false
                    )
                }

                scope.launch {
                    dataStore.saveTasks(tasks)
                }
            }
    }
}
