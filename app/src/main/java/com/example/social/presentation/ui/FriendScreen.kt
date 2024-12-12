package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.example.social.R
import com.example.social.db.userPostDataProvider

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FriendScreen(navController: NavController, friendViewModel: FriendViewModel
                 , friendRequestViewModel: FriendRequestViewModel
                 , friendSendViewModel: FriendSendViewModel, profileViewModel: ProfileViewModel, allUserViewModel: AllUserViewModel
) {

    val context= LocalContext.current
    val users = allUserViewModel.allUsers.collectAsState().value

    val friendRequests=friendRequestViewModel.friendRequests.collectAsState().value
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)
    friendRequestViewModel.getFriendRequests(Firebase.auth.currentUser!!.uid)
    friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)
    val userInfoList = friendRequestViewModel.userInfo.collectAsState().value

    val friends=friendViewModel.friends.collectAsState().value
    val friendSends=friendSendViewModel.friendSends.collectAsState().value
    val userIdRequests = mutableListOf<String>()
    val userIdSends = mutableListOf<String>()
    val userIds = mutableListOf<String>()

    if(friendRequests!=null) {
        for ((index, entry) in friendRequests.entries.withIndex()) {
            val friendRequestData = entry.value as? Map<*, *>
            val userId = friendRequestData?.get("uid") as? String
            val timestamp = friendRequestData?.get("timestamp") as Long
            if (userId != null) {
                userIdRequests.add(userId)
            }
        }
    }

    if(friendSends!=null) {
        for ((index, entry) in friendSends.entries.withIndex()) {
            val friendSendData = entry.value as? Map<*, *>
            val userId = friendSendData?.get("uid") as? String
            val timestamp = friendSendData?.get("timestamp") as Long
            if (userId != null) {
                userIdSends.add(userId)
            }
        }
    }

    if(friends!=null) {
        for ((index, entry) in friends.entries.withIndex()) {
            val friendData = entry.value as? Map<*, *>
            val userId = friendData?.get("uid") as? String
            val timestamp = friendData?.get("timestamp") as Long
            if (userId != null) {
                userIds.add(userId)
            }
        }
    }

    LazyColumn  (modifier=Modifier.fillMaxSize()){
        item{
            FirstLine(navController)
        }
        item{
            Text(text="Lời mời kết bạn"
                , color=colorResource(R.color.pink)
                , fontWeight = FontWeight.ExtraBold
                ,modifier=Modifier.padding(start = 10.dp))
            Spacer(Modifier.height(5.dp))

            friendRequestViewModel.getFriendInfo(userIdRequests)
            userInfoList.forEach{userInfo->
                FriendReqDisplay(userInfo,userIdRequests,navController,context,friendViewModel,friendRequestViewModel,friendSendViewModel)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { navController.navigate(Routes.ALL_FRIEND_REQ)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.white)
                    ),
                    modifier = Modifier
                        // Đặt kích thước cho nút
                        .border(
                            BorderStroke(1.dp, color = colorResource(R.color.pink)),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .size(width = 335.dp, height = 32.dp)

                ) {
                    Text(text = "Xem tất cả", color = colorResource(R.color.pink))
                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(320.dp)
                        .background(Color.Black.copy(alpha = 0.5f)) // Điều chỉnh độ mờ
                        .align(Alignment.Center) // Căn giữa
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(text="Gợi ý kết bạn",
                fontWeight = FontWeight.ExtraBold,
            )
        }
        items(users){user->
            if(user["uid"]!= Firebase.auth.currentUser!!.uid
                && user["uid"] !in userIdRequests
                && user["uid"] !in userIdSends
                && user["uid"] !in userIds ) {
                RecommendFriendTab(
                    user,
                    context,
                    friendViewModel,
                    friendRequestViewModel,
                    friendSendViewModel
                )
            }
        }
    }
}
@Composable
fun FirstLine(navController: NavController){
    Spacer(modifier = Modifier.height(16.dp))
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Row() {
            Text(
                text = "Bạn Bè", color = colorResource(R.color.pink),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.width(50.dp))

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier=Modifier.fillMaxWidth().padding(start = 10.dp)){
            Button(onClick = {navController.navigate(Routes.ALL_FRIEND)}, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.lightGrey)
            ),) {
                Text(text = "Bạn bè ", color = colorResource(R.color.pink))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(320.dp)
                    .background(Color.Black.copy(alpha = 0.5f)) // Điều chỉnh độ mờ
                    .align(Alignment.Center) // Căn giữa
            )
        }
    }
    Spacer(Modifier.height(16.dp))
}
@Composable
fun RecommendFriendTab(user: Map<String, Any>, context: Context
                       , friendViewModel: FriendViewModel
                       , friendRequestViewModel: FriendRequestViewModel, friendSendViewModel: FriendSendViewModel
) {

    var ispressed by remember { mutableStateOf<Boolean?>(false) }

    val name = "${user["firstname"]} ${user["lastname"]}"
    val avatarUri= Uri.parse(user["avatar"].toString())

    Spacer(Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        Box() {
            AsyncImage(
                model = avatarUri,
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(35.dp))
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text=name,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(5.dp))
            Row(){
                if(ispressed==false) {
                    Button(
                        onClick = {
                            friendRequestViewModel.updateFriendRequestToFirestore(user["uid"].toString(),
                                Firebase.auth.currentUser!!.uid)
                            friendSendViewModel.updateFriendSendToFirestore(Firebase.auth.currentUser!!.uid,user["uid"].toString())
                            ispressed=true
                            Toast.makeText(
                                context,
                                "Đã gửi lời mời kết bạn đến $name",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.pink)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            // Đặt kích thước cho nút
                            .size(width = 132.dp, height = 37.dp)
                            .clip(RectangleShape)
                    ) {
                        Text(text = "Thêm bạn", color = Color.White)
                    }
                }else {
                    Spacer(Modifier.height(2.dp))
                    Column {
                        Text(
                            text = "Đã gửi lời mời kết bạn",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                        Spacer(Modifier.height(2.dp))

                        Button(
                            onClick = {
                                friendRequestViewModel.deleteFriendReq(user["uid"].toString(),
                                    Firebase.auth.currentUser!!.uid)
                                friendRequestViewModel.getFriendRequests(user["uid"].toString())
                                friendSendViewModel.deleteFriendSend(Firebase.auth.currentUser!!.uid,user["uid"].toString())
                                friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)
                                ispressed = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.white)
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .fillMaxWidth().height(37.dp)

                        ) {
                            Text(text = "Hủy", color = colorResource(R.color.pink))
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}
@Composable
fun FriendSendDisplay(friend: Map<String,Any>, userIds:List<String>, navController: NavController, context: Context, friendViewModel: FriendViewModel, friendRequestViewModel: FriendRequestViewModel
                      , friendSendViewModel: FriendSendViewModel
) {
    var ispressed by remember { mutableStateOf<Boolean?>(false) }

    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val uid=friend["uid"].toString()

    Row(modifier = Modifier.fillMaxWidth()) {
        Box() {
            AsyncImage(
                model = avatarUri,
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(35.dp))
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {

            Row() {
                Text(text = name)
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Row() {
                if(ispressed==false)
                    Button(
                        onClick = {
                            friendSendViewModel.deleteFriendSend(Firebase.auth.currentUser!!.uid,uid)
                            friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)
                            friendRequestViewModel.deleteFriendReq(uid, Firebase.auth.currentUser!!.uid)
                            friendRequestViewModel.getFriendRequests(uid)
                            friendSendViewModel.getFriendInfo(userIds)
                            ispressed=true},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.white)
                        ),
                        modifier = Modifier
                            // Đặt kích thước cho nút
                            .border(
                                BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .fillMaxWidth().height(37.dp)

                    ) {
                        Text(text = "Hủy", color = colorResource(R.color.pink))
                    }
                else{
                    Spacer(Modifier.height(2.dp))
                    Text(text = "Đã hủy yêu cầu" ,
                        color =Color.Gray,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
    Spacer(Modifier.height(20.dp))
}



@Composable
fun FriendReqDisplay(friend: Map<String,Any>, userIds:List<String>, navController: NavController, context: Context, friendViewModel: FriendViewModel, friendRequestViewModel: FriendRequestViewModel
                     , friendSendViewModel: FriendSendViewModel
) {
    var actionTaken by remember { mutableStateOf<String?>(null) }

    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val uid=friend["uid"].toString()

    Row(modifier = Modifier.fillMaxWidth()) {
        Box() {
            AsyncImage(
                model =  avatarUri,
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(35.dp))
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {

            Row() {
                Text(text = name)
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Row() {
                Row() {
                    if(actionTaken==null) {
                        Spacer(Modifier.height(10.dp))
                        Button(
                            onClick = {
                                friendRequestViewModel.deleteFriendReq(Firebase.auth.currentUser!!.uid,uid)
                                friendSendViewModel.deleteFriendSend(uid, Firebase.auth.currentUser!!.uid)
                                friendViewModel.updateFriendToFirestore(Firebase.auth.currentUser!!.uid,uid)
                                friendViewModel.updateFriendToFirestore(uid, Firebase.auth.currentUser!!.uid)
                                actionTaken = "Đã chấp nhận kết bạn với $name" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.pink)
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .size(width = 132.dp, height = 37.dp)

                        ) {
                            Text(text = "Chấp nhận", color = Color.White)
                        }
                        Spacer(Modifier.weight(0.7f))
                        Button(
                            onClick = {
                                friendRequestViewModel.deleteFriendReq(Firebase.auth.currentUser!!.uid,uid)
                                friendSendViewModel.deleteFriendSend(uid, Firebase.auth.currentUser!!.uid)
                                actionTaken = "Đã từ chối kết bạn với $name" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .size(width = 132.dp, height = 37.dp)
                        ) {
                            Text(text = "Từ chối", color = colorResource(R.color.pink))
                        }
                    }else
                    {
                        Spacer(Modifier.height(2.dp))
                        Text(text = actionTaken!! ,
                            color =Color.Gray,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
    Spacer(Modifier.height(10.dp))
}
@Composable
fun GetHinhDaiDienFriend(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}