package com.example.tasktrackerandriod.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Task(
    val id: Int,
    var title: String,
    var isCompleted: Boolean = false
){
    // Wrap isCompleted in Compose state for automatic recomposition
    var completed by mutableStateOf(isCompleted)
}