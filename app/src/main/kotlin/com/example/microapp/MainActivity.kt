package com.example.microapp

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.view.Gravity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Bina XML file ke direct code se UI bana  rahe hain!
        val textView = TextView(this).apply {
            text = "Hello Achal! Native Kotlin App is Alive! 🔥"
            textSize = 24f
            gravity = Gravity.CENTER
        }
        
        setContentView(textView)
    }
}
