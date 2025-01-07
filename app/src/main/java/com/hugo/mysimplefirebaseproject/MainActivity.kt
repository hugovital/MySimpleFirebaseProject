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

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonClick).setOnClickListener {
            viewModel.fetchCatFact()
        }

        findViewById<Button>(R.id.buttonClose).setOnClickListener {
            // Kill the app completely
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

    private fun showFactDialog(fact: String) {
        AlertDialog.Builder(this)
            .setTitle("Cat Fact")
            .setMessage(fact)
            .setPositiveButton("OK", null)
            .show()
    }
}