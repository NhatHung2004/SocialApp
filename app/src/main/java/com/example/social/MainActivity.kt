package com.example.social

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.social.presentation.navigation.NavigationGraph
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.ThemeViewModel
import com.example.social.ui.theme.SocialTheme

class MainActivity : ComponentActivity() {
    // Khởi tạo ViewModel
    private val themeViewModel: ThemeViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                NavigationGraph(authViewModel, themeViewModel ,Modifier.padding(innerPadding))
            }
        }
    }
}