package com.example.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social.layouts.SignInScreen
import com.example.social.layouts.SignUpScreen
import com.example.social.layouts.TacVu2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SIGN_IN){
        composable(Routes.SIGN_IN) {
            SignInScreen(navController, Modifier)
        }
        composable(Routes.SIGN_UP) { SignUpScreen(navController) }
        composable(Routes.TAC_VU) { TacVu2(navController) }
    }
}