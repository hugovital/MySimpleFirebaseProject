package com.hugo.mysimplefirebaseproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to your custom view
        setContentView(CustomView(this))
    }
}