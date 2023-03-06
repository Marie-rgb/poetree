package com.mambo.poetree.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mambo.poetree.android.presentation.screens.MainScreen
import com.mambo.poetree.android.presentation.theme.PoetreeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            PoetreeTheme {
                MainScreen()
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    PoetreeTheme {
        MainScreen()
    }
}
