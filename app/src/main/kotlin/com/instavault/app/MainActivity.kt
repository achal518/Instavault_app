package com.instavault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.instavault.app.ui.navigation.AppNavigation
import com.instavault.app.ui.theme.InstavaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstavaultTheme {
                AppNavigation()
            }
        }
    }
}
