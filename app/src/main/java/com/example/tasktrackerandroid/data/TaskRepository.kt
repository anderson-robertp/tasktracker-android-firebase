package com.example.tasktrackerandroid.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import android.content.Context
import com.example.tasktrackerandroid.TaskList
import com.example.tasktrackerandroid.data.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

object TaskPreferencesSerializer : Serializer<TaskList> {
    override val defaultValue: TaskList = TaskList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TaskList =
        TaskList.parseFrom(input)

    override suspend fun writeTo(t: TaskList, output: OutputStream) =
        t.writeTo(output)
}

val Context.taskDataStore: DataStore<TaskList> by dataStore(
    fileName = "tasks.pb",
    serializer = TaskPreferencesSerializer
)

class TaskRepositoryDB (
    private val db: FirebaseFirestore,
    private val dataStore: TaskDataStore
){
    // Flow that return local cached tasks
    val cachedTaskFlow: Flow<List<Task>> = dataStore.tasksFlow

    // Listen to Firestore changes and updates
    fun startSync() {
        db.collection("tasks").addSnapshotListener { snapshot,error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val tasks = snapshot.documents.map { doc ->
                    Task(
                        id = doc.id.toInt(),
                        title = doc.getString("title"),
                        isCompleted = doc.getBoolean("isCompleted") ?: false
                    )
                }
                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.saveTasks(tasks)
                }
            }
        }
    }

    // Write to Firestore
    suspend fun addOrUpdateTask(task: Task) {
        db.collection("tasks")
            .document(task.id.toString())
            .set(task)
        dataStore.saveTasks(listOf(task))
    }

    // Delete from Firestore
    suspend fun deleteTask(task: Task) {
        db.collection("tasks")
            .document(task.id.toString())
            .delete()
    }

}