package com.example.tasktrackerandroid

import com.example.tasktrackerandroid.viewmodel.TaskViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: FakeTaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = FakeTaskRepository()
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addTask_updatesTaskList() = runTest {
        viewModel.addTask("Test Task")
        advanceUntilIdle()

        val tasks = viewModel.tasks.value
        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks.first().title)
    }

    @Test
    fun toggleTask_updatesCompletion() = runTest {
        viewModel.addTask("Task")
        advanceUntilIdle()

        val taskId = viewModel.tasks.value.first().id
        viewModel.toggleTaskComplete(taskId)
        advanceUntilIdle()

        assertTrue(viewModel.tasks.value.first().isCompleted)
    }
}
