package com.example.social.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social.presentation.ui.LoginScreen
import com.example.social.presentation.ui.RegisterScreen
import com.example.social.presentation.ui.TabScreen
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(authViewModel: AuthViewModel = viewModel(), themeViewModel: ThemeViewModel, modifier: Modifier) {
    val navController = rememberNavController()

    // Điều hướng dựa trên trạng thái người dùng (đã đăng nhập hay chưa)
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(authViewModel, navController)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(authViewModel, navController)
        }
        composable(Routes.TABS) {
            TabScreen(navController, authViewModel, themeViewModel)
        }
    }
}