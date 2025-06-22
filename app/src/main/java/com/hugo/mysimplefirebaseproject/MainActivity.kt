package com.hugo.mysimplefirebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.hugo.mysimplefirebaseproject.presentation.MainViewModel
import com.hugo.mysimplefirebaseproject.presentation.MainViewModelFactory
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.app.FrameMetricsAggregator
import com.hugo.mysimplefirebaseproject.certs.CustomTrustManager
import com.hugo.mysimplefirebaseproject.certs.OkHttpClientProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    private lateinit var client: OkHttpClient

    var newIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //val aggregator = FrameMetricsAggregator()
        //aggregator.add(this)

        val btnIncompleteChain = findViewById<Button>(R.id.btnIncompleteChain)
        val checkboxUseAIA = findViewById<CheckBox>(R.id.checkboxUseAIA)

        btnIncompleteChain.setOnClickListener {
            client = if (checkboxUseAIA.isChecked) {
                OkHttpClientProvider.getSecureClient()
            } else {
                OkHttpClient()
            }
            
            lifecycleScope.launch {
                testIncompleteChain()
            }
        }

        findViewById<Button>(R.id.buttonClick).setOnClickListener {

            var list = mutableListOf<String>()
            val thread = Thread {
                while ( true ){
                    var s : String = "CAWABANGA"
                    //println("Hello from thread")
                    list.add(s)
                    //Thread.sleep(1);
                }

            }

            //thread.start()

            viewModel.fetchCatFact()
        }

        findViewById<Button>(R.id.buttonClose).setOnClickListener {
            finishAffinity()
            System.exit(0)
        }

        val stringList = mutableListOf<String>()

        findViewById<Button>(R.id.btnViewTests).setOnClickListener {

            try {

                for (i in 0..10000000) {
                    stringList.add("CAWABANGA")
                }

                /*
                AlertDialog.Builder(this)
                    .setMessage("Testes " + stringList.size)
                    .setPositiveButton("OK", null)
                    .show()
                 */


                //val intent = Intent(this, CustomViewActivity::class.java)

                newIntent = newIntent ?: Intent(this, CustomViewActivity::class.java)
                startActivity(newIntent)

            } catch (e : Exception){
                System.out.println("CAWABANGA");
                e.printStackTrace();
                System.out.println("/CAWABANGA");

            }
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
        var url = "https://mobilets8aws.rdhi.com.br:9443/";
        url = "https://mobilets8aws.rdhi.com.br:443/";
        //url = "https://incomplete-chain.badssl.com/";

        try {
            withContext(Dispatchers.IO) {
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                
                // Show success dialog with response details
                withContext(Dispatchers.Main) {
                    showSuccessDialog("Status Code: ${response.code}\nMessage: ${response.message}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.IO) {
                CustomTrustManager.runSSLCall(url);
            }
            showErrorDialog("URL: " + url + " - " + e.message ?: "Unknown error occurred")
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

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
