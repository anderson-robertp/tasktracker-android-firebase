# Overview

This project is an Android task-tracking application built to strengthen my skills in modern mobile development practices, cloud integration, and state-driven UI architecture. My goal was to build a clean, scalable solution using Jetpack Compose, Material 3, MVVM, Firebase services, and DataStore with Protocol Buffers.

The app allows users to create an account, log in, and manage a synchronized task list. Tasks are stored both locally using Proto DataStore and in the cloud using Firebase Realtime Database. This ensures the app works offline while keeping data synced when connectivity returns.

Users can add, delete, edit, and toggle tasks. The UI is reactive, intuitive, and built entirely in Compose.

This project uses Firebase Realtime Database for cloud persistence and Firebase Authentication for user identity.

The database is synchronized with local Proto DataStore so the app continues functioning offline and re-syncs when the device reconnects.

[Software Demo Video](https://youtu.be/YfKzbRNoNRs)

# Development Environment

- **Android Studio Otter**
- **Jetpack Compose** for UI  
- **Kotlin** programming language  
- **AndroidX Navigation Compose** for multi-screen routing  
- **Proto DataStore** for local persistent storage  
- **Material 3** for UI  
- **Gradle Build System**  
- Tested on **Android Emulator** and physical device
- Firebase Console

# Useful Websites

- [Android Developers – Jetpack Compose](https://developer.android.com/jetpack/compose)  
- [Android Developers – DataStore](https://developer.android.com/topic/libraries/architecture/datastore)  
- [Android Developers – Navigation Compose](https://developer.android.com/jetpack/compose/navigation)  
- [Kotlin Language Documentation](https://kotlinlang.org/docs/home.html)  
- [Material 3 Components](https://m3.material.io/)  
- [Firebase Documentation](https://firebase.google.com/docs)

# Future Work
 
- Add task categories or tagging  
- Add reminders or notifications  
- Support dark/light theme switching  
- Enable drag-and-drop task reordering  
- Improve UI with animations
- Add push notifications for reminders
- Add profile settings UI for managing user accounts
- Improve error handling for network failures
- Expand database rules for stricter 

# Quick Start
1) Clone the repository
   - [Repository](https://github.com/anderson-robertp/tasktracker-android-firebase.git)
2) Open in Android Studio
3) Add Your google-services.
    - Firebase requires this file for authentication and database access.
      In the Firebase Console, create a new Android app
    - Download the google-services.json file
    - Drag and drop into the project app/google-services.json
4) Build the Project
5) Run the App
6) Create an account and sign in
