package com.example.tasktrackerandroid.data

import android.util.Log
import com.example.tasktrackerandroid.data.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class FirebaseTaskService @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dataStore: TaskDataStore
) : TaskRepository {

    override fun observeTasks(): Flow<List<Task>> = callbackFlow {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        val listener = db.collection("users")
            .document(userId)
            .collection("tasks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tasks = snapshot?.documents?.map { doc ->
                    Task(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        isCompleted = doc.getBoolean("isCompleted") ?: false
                    )
                }.orEmpty()

                trySend(tasks)
            }

        awaitClose { listener.remove() }
    }


    override fun startSync(scope: CoroutineScope) {
        scope.cancel()

        scope.launch {
            observeTasks().collect { taskList ->
                dataStore.saveTasks(taskList)
            }
        }
    }

    override fun tasks(): Flow<List<Task>> {
        Log.d("FirebaseTaskService", "tasks() called")
        return observeTasks()
    }

    override suspend fun addTask(task: Task): Result<Unit> {
        return try {
            val userId = userId()

            val docRef = db.collection("users")
                .document(userId)
                .collection("tasks")
                .document()
            val taskWithId = task.copy(id = docRef.id)

            docRef.set(taskWithId).await()
            Log.d("FirebaseTaskService", "Task added: $task")
            Result.success(Unit)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editTask(taskId: String, newTitle: String?) {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        db.collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId)
            .update("title", newTitle)
            .await()
        Log.d("FirebaseTaskService", "Task edited: $taskId")
    }

    override suspend fun deleteTask(taskId: String) {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        db.collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId)
            .delete()
            .await()
        Log.d("FirebaseTaskService", "Task deleted: $taskId")
    }

    override suspend fun toggleTaskComplete(taskId: String, isCompleted: Boolean) {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        db.collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId)
            .update("isCompleted", isCompleted)
            .await()
        Log.d("FirebaseTaskService", "Task toggled: $taskId")
    }

    private fun userId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
    }
}
