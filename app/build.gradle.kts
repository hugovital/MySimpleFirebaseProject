plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.firebase-perf") }

android {
    namespace = "com.hugo.mysimplefirebaseproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hugo.mysimplefirebaseproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Add Firebase Performance configuration here
        manifestPlaceholders += mapOf(
            "firebase_performance_collection_enabled" to "true",
            "firebase_performance_collection_deactivated" to "false"
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            manifestPlaceholders["firebase_performance_logcat_enabled"] = true
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")
    // Coroutines
    //implementation("org.jetbrains.kotlinx:coroutines-android:1.7.3")
    //implementation("org.jetbrains.kotlinx:coroutines-core:1.7.3")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Add Firebase Performance Monitoring
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Add Datadog dependencies
    implementation("com.datadoghq:dd-sdk-android-rum:2.17.0")
    implementation("com.datadoghq:dd-sdk-android-okhttp:2.17.0")

}