package com.hugo.mysimplefirebaseproject

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
import com.datadog.android.rum.tracking.ActivityViewTrackingStrategy
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryClassInMB = activityManager.memoryClass  // Standard heap size in MB
        val largeMemoryClassInMB = activityManager.largeMemoryClass  // Heap size when using largeHeap

        System.out.println("MEMORY - memoryClassInMB: " + memoryClassInMB);
        System.out.println("MEMORY - largeMemoryClassInMB: " + largeMemoryClassInMB);

        val totalMemoryInBytes = getTotalDeviceMemory(super.getBaseContext())
        val totalMemoryInMB = totalMemoryInBytes / (1024 * 1024)
        System.out.println("MEMORY: Total device memory: $totalMemoryInMB MB")

        FirebaseApp.initializeApp(this)
        initializeDatadog()
    }

    fun getTotalDeviceMemory(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem  // Total memory in bytes
    }

    private fun initializeDatadog() {
        val clientToken = "pubdd776b1f18bf37c0b8408961341b93b6"
        val environmentName = "PROD"
        val appVariantName = "MyFirebaseSimpleApp"

        val configuration = Configuration.Builder(
            clientToken = clientToken,
            env = environmentName,
            variant = appVariantName
        )
            .useSite(DatadogSite.US1)
            .build()
        Datadog.initialize(this, configuration, TrackingConsent.GRANTED)

        val applicationId = "4eb6e9ff-6e0b-4b2f-a2b8-0001d91c0d28"

        val rumConfiguration = RumConfiguration.Builder(applicationId)
            .trackUserInteractions()
            .trackLongTasks(100)
            .useViewTrackingStrategy(ActivityViewTrackingStrategy(true))
            // Default resource tracking is enabled by the SDK
            .build()
        Rum.enable(rumConfiguration)
    }
}