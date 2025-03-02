// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
    id("com.datadoghq.dd-sdk-android-gradle-plugin") version "1.14.0" apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
}