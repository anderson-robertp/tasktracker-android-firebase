package com.example.tasktrackerandroid.data.datastore

import androidx.datastore.core.Serializer
import com.example.tasktrackerandroid.TaskList
import java.io.InputStream
import java.io.OutputStream

/**
 * A serializer for the `TaskList` Protobuf type.
 *
 * This object tells DataStore how to read and write the `TaskList` data object
 * from a physical file. It implements the `Serializer` interface, providing the
 * logic for converting the data object to and from an `InputStream`/`OutputStream`.
 */
object TaskListSerializer : Serializer<TaskList> {
    /**
     * The default value to be used when the DataStore file does not yet exist.
     * This ensures that the first read operation on a new DataStore file
     * returns a valid, non-null, empty `TaskList` object instead of throwing an error.
     */
    override val defaultValue: TaskList = TaskList.getDefaultInstance()
    /**
     * Defines how to read the data from an `InputStream` and parse it back
     * into a `TaskList` object.
     *
     * DataStore calls this function when reading data from the file.
     * The `parseFrom` method is an auto-generated function from Protobuf.
     *
     * @param input The `InputStream` from the file on disk.
     * @return The parsed `TaskList` object.
     */
    override suspend fun readFrom(input: InputStream): TaskList {
        // Read the data from the input stream and parse it into a TaskList object.
        return TaskList.parseFrom(input)
    }

    /**
     * Defines how to write the `TaskList` object to an `OutputStream`.
     *
     * DataStore calls this function when persisting the data to a file.
     *
     * @param t The `TaskList` object to be written to disk.
     * @param output The `OutputStream` to the file on disk.
     */
    override suspend fun writeTo(t: TaskList, output: OutputStream) {
        // Write the TaskList object to the output stream.
        t.writeTo(output)
    }
}
