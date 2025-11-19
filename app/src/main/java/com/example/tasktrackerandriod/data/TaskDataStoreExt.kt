package com.example.tasktrackerandriod.data

import android.content.Context

// Extension property to access TaskDataStore from any Context
val Context.protoTaskDataStore: TaskDataStore
    get() = TaskDataStore(this)
