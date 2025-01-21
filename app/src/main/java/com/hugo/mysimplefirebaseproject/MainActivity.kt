package com.hugo.mysimplefirebaseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.hugo.mysimplefirebaseproject.presentation.MainViewModel
import com.hugo.mysimplefirebaseproject.presentation.MainViewModelFactory
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonClick).setOnClickListener {
            viewModel.fetchCatFact()
        }

        findViewById<Button>(R.id.buttonIncompleteChain).setOnClickListener {
            lifecycleScope.launch {
                testIncompleteChain()
            }
        }

        findViewById<Button>(R.id.buttonClose).setOnClickListener {
            finishAffinity()
            System.exit(0)
        }

        // Observe cat facts
        lifecycleScope.launch {
            viewModel.catFact.collect { fact ->
                fact?.let {
                    showFactDialog(it)
                }
            }
        }
    }

    private suspend fun testIncompleteChain() {
        try {
            withContext(Dispatchers.IO) {
                val request = Request.Builder()
                    .url("https://incomplete-chain.badssl.com/")
                    .build()

                client.newCall(request).execute()
            }
        } catch (e: Exception) {
            showErrorDialog(e.message ?: "Unknown error occurred")
        }
    }

    private fun showFactDialog(fact: String) {
        AlertDialog.Builder(this)
            .setTitle("Cat Fact")
            .setMessage(fact)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showErrorDialog(error: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Close", null)
            .show()
    }
}
