package com.example.tasktrackerandroid.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Task(
    val id: String = "",
    var title: String?,
    var isCompleted: Boolean = false
){
    // Wrap isCompleted in Compose state for automatic recomposition
    var isChecked by mutableStateOf(isCompleted)
}