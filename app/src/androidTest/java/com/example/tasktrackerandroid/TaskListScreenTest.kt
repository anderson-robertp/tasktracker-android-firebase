package com.example.tasktrackerandroid

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.tasktrackerandroid.ui.screens.TaskListScreen
import com.example.tasktrackerandroid.viewmodel.TaskViewModel
import org.junit.Rule
import org.junit.Test

@get:Rule
val composeRule = createComposeRule()

@Test
fun addTask_showsInList() {
    val fakeRepo = FakeTaskRepository()
    val viewModel = TaskViewModel(fakeRepo)

    composeRule.setContent {
        TaskListScreen(
            viewModel = viewModel,
            navController = rememberNavController(),
            onLogout = {}
        )
    }

    composeRule.onNodeWithText("New Task")
        .performTextInput("Hello World")

    composeRule.onNodeWithText("Add Task")
        .performClick()

    composeRule.onNodeWithText("Hello World")
        .assertExists()
}
