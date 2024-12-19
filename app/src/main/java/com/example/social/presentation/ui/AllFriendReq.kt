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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
fun AllFriendReq(navController: NavController
                 , friendViewModel: FriendViewModel
                 , friendRequestViewModel: FriendRequestViewModel
                 , friendSendViewModel: FriendSendViewModel,
                 notificationViewModel: NotificationViewModel){

    val context= LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)

    friendRequestViewModel.getFriendRequests(Firebase.auth.currentUser!!.uid)
    val friendRequests=friendRequestViewModel.friendRequests.collectAsState().value

    val userInfoList = friendRequestViewModel.userInfo.collectAsState().value
    val userIdRequests = mutableListOf<Friend>()
    if(friendRequests!=null) {
        for ((index, entry) in friendRequests.entries.withIndex()) {
            val friendRequestData = entry.value as? Map<*, *>
            val userId = friendRequestData?.get("uid") as? String
            val timestamp = friendRequestData?.get("timestamp") as Long
            if (userId != null) {
                userIdRequests.add(Friend(userId,timestamp))
            }
        }
    }

    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    navController.navigate(Routes.FRIEND)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Lời mời kết bạn",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
            Spacer(Modifier.weight(1f))
            Button(onClick = {showBottomSheet.value=true}
                ,modifier=Modifier.padding(end = 2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),){
                Image(
                    painter = painterResource(R.drawable.meatballsmenuc),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = "Lời mời kết bạn", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = userIdRequests.size.toString(), fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color= colorResource(R.color.pink)
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn (modifier=Modifier.fillMaxSize().padding(start=6.dp)){
            item{
                friendRequestViewModel.getFriendInfo(userIdRequests.map { it.uid })
                userInfoList.forEach{userInfo->
                    val friendData=userIdRequests.find{it.uid==userInfo["uid"]}
                    if (friendData != null) {
                        FriendReqDisplay(userInfo,userIdRequests.map { it.uid },friendData.timestamp,friendViewModel,friendRequestViewModel,friendSendViewModel,notificationViewModel,notificationContents)
                    }
                }
            }
        }
    }
    if (showBottomSheet.value) {
        FriendReqToSendBottomSheet(navController , onDismiss = {showBottomSheet.value=false})
    }
}
