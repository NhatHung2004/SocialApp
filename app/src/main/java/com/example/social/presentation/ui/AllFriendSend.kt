package com.example.social.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.social.R
import com.example.social.data.model.Friend
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllFriendSend(navController: NavController, friendViewModel: FriendViewModel,
                  friendRequestViewModel: FriendRequestViewModel,
                  friendSendViewModel: FriendSendViewModel,
                  notificationViewModel: NotificationViewModel){

    val context = LocalContext.current

    val friendSends=friendSendViewModel.friendSends.collectAsState().value
    friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)

    val userInfoList = friendSendViewModel.userInfo.collectAsState().value
    val userIdSends = mutableListOf<Friend>()

    if(friendSends!=null) {
        for ((index, entry) in friendSends.entries.withIndex()) {
            val friendSendData = entry.value as? Map<*, *>
            val userId = friendSendData?.get("uid") as? String
            val timestamp = friendSendData?.get("timestamp") as Long
            if (userId != null) {
                userIdSends.add(Friend(userId,timestamp))
            }
        }
    }

    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    navController.navigate(Routes.ALL_FRIEND_REQ)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Lời mời đã gửi",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = "Lời mời đã gửi", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = userIdSends.size.toString(), fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color= colorResource(R.color.pink)
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn (modifier=Modifier.fillMaxSize().padding(start=6.dp)){
            item {
                friendSendViewModel.getFriendInfo(userIdSends.map {it.uid})
                userInfoList.forEach{userInfo->
                    val friendData=userIdSends.find{it.uid==userInfo["uid"]}
                    if (friendData != null) {
                        FriendSendDisplay(userInfo,userIdSends.map { it.uid },friendData.timestamp,
                            navController,context,friendViewModel,friendRequestViewModel,friendSendViewModel, notificationViewModel )
                    }
                }
            }
        }
    }
}
