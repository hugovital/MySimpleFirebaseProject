package com.hugo.mysimplefirebaseproject.data.api

import com.datadog.android.okhttp.DatadogEventListener
import com.datadog.android.okhttp.DatadogInterceptor
import com.datadog.android.trace.TracingHeaderType
import com.hugo.mysimplefirebaseproject.data.model.CatFact
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class CatFactApi {
    private val client: OkHttpClient

    init {
        val tracedHostsWithHeaderType = mapOf(
            "example.com" to setOf(
                TracingHeaderType.DATADOG,
                TracingHeaderType.TRACECONTEXT),
            "example.eu" to setOf(
                TracingHeaderType.DATADOG,
                TracingHeaderType.TRACECONTEXT))

        client = OkHttpClient.Builder()
            .addInterceptor(DatadogInterceptor.Builder(tracedHostsWithHeaderType).build())
            .eventListenerFactory(DatadogEventListener.Factory())
            .build()
    }

    private val gson = Gson()
    private val url = "https://catfact.ninja/fact"

    suspend fun getCatFact(): Result<CatFact> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            val json = response.body?.string()
            
            if (response.isSuccessful && !json.isNullOrEmpty()) {
                Result.success(gson.fromJson(json, CatFact::class.java))
            } else {
                Result.failure(IOException("Failed to fetch cat fact"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 