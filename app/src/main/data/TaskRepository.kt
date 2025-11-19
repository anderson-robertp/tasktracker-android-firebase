package com.example.tasktrackerandriod.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import android.content.Context
import com.example.tasktrackerandriod.TaskList
import com.example.tasktrackerandriod.TaskProto
import java.io.InputStream
import java.io.OutputStream

object TaskListSerializer : Serializer<TaskList> {
    override val defaultValue: TaskList = TaskList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TaskList =
        TaskList.parseFrom(input)

    override suspend fun writeTo(t: TaskList, output: OutputStream) =
        t.writeTo(output)
}

val Context.taskDataStore: DataStore<TaskList> by dataStore(
    fileName = "tasks.pb",
    serializer = TaskListSerializer
)