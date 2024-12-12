package com.example.social.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social.presentation.ui.AllFriend
import com.example.social.presentation.ui.AllFriendReq
import com.example.social.presentation.ui.AllFriendSend
import com.example.social.presentation.ui.FriendScreen
import com.example.social.presentation.ui.HomeScreen
import com.example.social.presentation.ui.LoginScreen
import com.example.social.presentation.ui.ProfileEdit
import com.example.social.presentation.ui.ProfileFriendScreen
import com.example.social.presentation.ui.ProfileScreen
import com.example.social.presentation.ui.RegisterScreen
import com.example.social.presentation.ui.StatusScreen
import com.example.social.presentation.ui.TabScreen
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel

@Composable
fun NavigationTab(startDestination:String
                  ,profileViewModel: ProfileViewModel
                  ,postViewModel: PostViewModel
                  ,friendViewModel: FriendViewModel
                  ,friendRequestViewModel: FriendRequestViewModel
                  ,friendSendViewModel: FriendSendViewModel
                  ,commentViewModel: CommentViewModel,
                  allUserViewModel: AllUserViewModel,
                  authViewModel: AuthViewModel,
                  navControllerAll: NavController
){
    val navController = rememberNavController()

    // Điều hướng dựa trên trạng thái người dùng (đã đăng nhập hay chưa)
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.HOME) {
            HomeScreen(navController,friendViewModel,profileViewModel)
        }
        composable(Routes.FRIEND_PROFILE + "/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                ProfileFriendScreen(navController, userId, profileViewModel, postViewModel, friendViewModel,commentViewModel)
            }
        }
        composable(Routes.FRIEND) {
            FriendScreen(navController,friendViewModel,friendRequestViewModel, friendSendViewModel, profileViewModel, allUserViewModel)
        }
        composable(Routes.ALL_FRIEND_REQ) {
            AllFriendReq(navController,friendViewModel, friendRequestViewModel, friendSendViewModel)
        }
        composable(Routes.ALL_FRIEND_SEND) {
            AllFriendSend(navController,friendViewModel, friendRequestViewModel, friendSendViewModel)
        }
        composable(Routes.ALL_FRIEND) {
            AllFriend(friendViewModel, friendRequestViewModel, friendSendViewModel)
        }
        composable(Routes.STATUS) {
            StatusScreen(navController,profileViewModel,postViewModel)
        }
        composable(Routes.PROFILE_SCREEN) {
            ProfileScreen(navControllerAll, navController, authViewModel,
                commentViewModel, postViewModel, profileViewModel,friendViewModel)
        }
        composable(Routes.ALL_FRIEND) {
            AllFriend(friendViewModel, friendRequestViewModel, friendSendViewModel)
        }
        composable(Routes.PROFILE_EDIT) {
            ProfileEdit(navController, profileViewModel)
        }
    }
}