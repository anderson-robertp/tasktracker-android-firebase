package com.example.tasktrackerandroid

import com.example.tasktrackerandroid.viewmodel.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Test
fun login_success_setsLoggedIn() = runTest {
    val fakeAuthRepo = FakeAuthRepository(success = true)
    val vm = AuthViewModel(fakeAuthRepo, FakeTaskDataStore())

    vm.login("test@test.com", "password")
    advanceUntilIdle()

    assertTrue(vm.state.value.isLoggedIn)
}

@Test
fun logout_clearsLocalTasks() = runTest {
    val fakeDataStore = FakeTaskDataStore()
    val fakeAuthRepo = FakeAuthRepository(success = true)

    val viewModel = AuthViewModel(fakeAuthRepo, fakeDataStore)

    fakeDataStore.saveTasks(
        listOf(Task(id = "1", title = "Test"))
    )

    viewModel.logout()

    assertTrue(fakeDataStore.tasks.first().isEmpty())
}
