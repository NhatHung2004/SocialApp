package com.example.social.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social.presentation.ui.AllFriend
import com.example.social.presentation.ui.AllFriendReq
import com.example.social.presentation.ui.AllFriendSend
import com.example.social.presentation.ui.FriendScreen
import com.example.social.presentation.ui.ProfileEdit
import com.example.social.presentation.ui.ProfileScreen
import com.example.social.presentation.ui.SettingsScreen


@Composable
fun NavigationTab(navController: NavController,startDestination:String){
    val navControllerTab= rememberNavController()
    NavHost(navController = navControllerTab, startDestination = startDestination) {
        composable(Routes.FRIEND) {
            FriendScreen(navController)
        }
        composable(Routes.ALL_FRIEND_REQ) {
            AllFriendReq(navController)
        }
        composable(Routes.ALL_FRIEND_SEND) {
            AllFriendSend()
        }
        composable(Routes.ALL_FRIEND) {
            AllFriend(navControllerTab)
        }
        composable(Routes.PROFILE_SCREEN) {
            ProfileScreen(navController,navControllerTab)
        }
        composable(Routes.PROFILE_EDIT) {
            ProfileEdit(navControllerTab)
        }
        composable(Routes.SETTING) {
            SettingsScreen()
        }
    }
}