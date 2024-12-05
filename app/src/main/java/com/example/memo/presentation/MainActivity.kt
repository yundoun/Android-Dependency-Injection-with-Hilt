// presentation/MainActivity.kt
package com.example.memo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.memo.presentation.navigation.NavGraph
import com.example.memo.presentation.theme.MemoTheme
import dagger.hilt.android.AndroidEntryPoint

// Activity에서의 Hilt 설정
@AndroidEntryPoint // Hilt가 의존성을 주입할 수있게 함
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}