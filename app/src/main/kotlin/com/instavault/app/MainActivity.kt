package com.instavault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.instavault.app.ui.navigation.AppNavigation
import com.instavault.app.ui.theme.InstavaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            InstavaultTheme {
                AppNavigation()
            }
        }
    }
}
