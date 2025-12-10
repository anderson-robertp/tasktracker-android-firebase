package com.example.tasktrackerandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasktrackerandroid.ui.theme.TaskTrackerAndroidV2Theme
import com.example.tasktrackerandroid.viewmodel.TaskViewModel
import androidx.navigation.compose.rememberNavController
import com.example.tasktrackerandroid.navigation.AppNavHost
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tasktrackerandroid.viewmodel.AuthViewModel
import com.google.firebase.perf.session.SessionManager

/**
 * The main and only activity in this single-activity architecture application.
 * It serves as the entry point for the app and hosts all the Jetpack Compose UI content.
 */
class MainActivity : ComponentActivity() {

    /**
     * Lazily initializes the [TaskViewModel].
     * The `by viewModels()` delegate ensures that the ViewModel is scoped to this activity,
     * meaning it survives configuration changes (like screen rotation) and is shared
     * across all composables hosted by this activity.
     */
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private val authViewModel: AuthViewModel by viewModels()


    /**
     * The entry point method called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge layout for the activity.
        enableEdgeToEdge()
        // Sets the content of the activity using Jetpack Compose.
        setContent {
            // The main theme for the app.
            TaskTrackerAndroidV2Theme {
                // A surface container using the 'background' color from the theme.
                Surface(modifier = Modifier.fillMaxSize()) {
                    // creates a [Scaffold] with a [TaskScreen] as its content.
                    val navController = rememberNavController()
                    val isLoggedIn by authViewModel.state.collectAsState()
                    AppNavHost(
                        navController = navController,
                        taskViewModel = taskViewModel,
                        authViewModel = authViewModel,
                        isLoggedIn = isLoggedIn.isLoggedIn
                    )

                }
            }
        }
    }
}

/**
 * A simple composable function for demonstration and preview purposes.
 * It displays a greeting text.
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
/**
 * A preview composable that allows developers to see how the `Greeting` composable
 * looks within the Android Studio editor without running the app on a device.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskTrackerAndroidV2Theme {
        Greeting("Android")
    }
}