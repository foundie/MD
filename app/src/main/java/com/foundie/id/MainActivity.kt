package com.foundie.id

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.foundie.id.ui.navigation.MainContent
import com.foundie.id.ui.navtheme.MyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MainContent()
            }
        }
    }
}
