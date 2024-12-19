package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import com.example.social.R
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.social.data.model.Friend
import com.example.social.domain.utils.toPrettyTime
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FriendScreen(navController: NavController, friendViewModel: FriendViewModel
                 , friendRequestViewModel: FriendRequestViewModel
                 , friendSendViewModel: FriendSendViewModel, allUserViewModel: AllUserViewModel
                 , notificationViewModel: NotificationViewModel
) {

    val context= LocalContext.current
    allUserViewModel.getAllUsersInfo()
    val users = allUserViewModel.allUsers.collectAsState().value
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)


    val friendRequests=friendRequestViewModel.friendRequests.collectAsState().value
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)
    friendRequestViewModel.getFriendRequests(Firebase.auth.currentUser!!.uid)
    friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)
    val userInfoList = friendRequestViewModel.userInfo.collectAsState().value

    val friends=friendViewModel.friends.collectAsState().value
    val friendSends=friendSendViewModel.friendSends.collectAsState().value
    val userIdRequests = mutableListOf<Friend>()
    val userIdSends = mutableListOf<Friend>()
    val userIds = mutableListOf<Friend>()

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

    if(friends!=null) {
        for ((index, entry) in friends.entries.withIndex()) {
            val friendData = entry.value as? Map<*, *>
            val userId = friendData?.get("uid") as? String
            val timestamp = friendData?.get("timestamp") as Long
            if (userId != null) {
                userIds.add(Friend(userId,timestamp))
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

            friendRequestViewModel.getFriendInfo(userIdRequests.map { it.uid })
            userInfoList.forEach{userInfo->
                val friendData=userIdRequests.find{it.uid==userInfo["uid"]}
                if (friendData != null) {
                    FriendReqDisplay(userInfo,userIdRequests.map { it.uid },friendData.timestamp,friendViewModel,friendRequestViewModel,friendSendViewModel,notificationViewModel,notificationContents)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { navController.navigate(Routes.ALL_FRIEND_REQ)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ),
                    modifier = Modifier
                        // Đặt kích thước cho nút
                        .border(
                            BorderStroke(1.dp, color = colorResource(R.color.lightGrey)),
                            shape = RoundedCornerShape(15.dp)
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
                && user["uid"] !in userIdRequests.map { it.uid }
                && user["uid"] !in userIdSends.map { it.uid }
                && user["uid"] !in userIds.map { it.uid } ) {
                RecommendFriendTab(
                    user,
                    context,
                    friendRequestViewModel,
                    friendSendViewModel,
                    notificationViewModel,
                    notificationContents
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
                       , friendRequestViewModel: FriendRequestViewModel, friendSendViewModel: FriendSendViewModel
                       , notificationViewModel: NotificationViewModel
                       , notificationContents: Array<String>
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
                            notificationViewModel.updateNotificationToFireStore(
                                Firebase.auth.currentUser!!.uid,"",notificationContents[0],"notRead",user["uid"].toString())
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
                                friendSendViewModel.deleteFriendSend(Firebase.auth.currentUser!!.uid,user["uid"].toString())
                                notificationViewModel.deleteFriendNotification(user["uid"].toString(),Firebase.auth.currentUser!!.uid)
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendSendDisplay(friend: Map<String,Any>, userIds:List<String>,time:Long, navController: NavController, context: Context, friendViewModel: FriendViewModel, friendRequestViewModel: FriendRequestViewModel
                      , friendSendViewModel: FriendSendViewModel,notificationViewModel: NotificationViewModel
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
                Text(text= time.toPrettyTime())
            }
            Spacer(Modifier.height(10.dp))
            Row() {
                if(ispressed==false)
                    Button(
                        onClick = {
                            friendSendViewModel.deleteFriendSend(Firebase.auth.currentUser!!.uid,uid)
                            friendRequestViewModel.deleteFriendReq(uid, Firebase.auth.currentUser!!.uid)
                            notificationViewModel.deleteFriendNotification(uid,Firebase.auth.currentUser!!.uid)
                            ispressed=true},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
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



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendReqDisplay(friend: Map<String,Any>, userIds:List<String>,time:Long, friendViewModel: FriendViewModel, friendRequestViewModel: FriendRequestViewModel
                     , friendSendViewModel: FriendSendViewModel, notificationViewModel: NotificationViewModel, notificationContents: Array<String>
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
                    .size(75.dp)
                    .clip(RoundedCornerShape(35.dp))
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Row() {
                Text(text = name,color=MaterialTheme.colorScheme.onBackground)
            }
            Spacer(Modifier.height(2.dp))
            Text(text= time.toPrettyTime())
            Spacer(Modifier.height(2.dp))
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
                                notificationViewModel.updateNotificationToFireStore(Firebase.auth.currentUser!!.uid,
                                    "",
                                    notificationContents[1],
                                    "notRead",
                                    uid)
                                notificationViewModel.deleteFriendNotification(Firebase.auth.currentUser!!.uid,uid)
                                actionTaken = "Đã chấp nhận kết bạn với $name" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.pink)
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .size(width = 132.dp, height = 37.dp)
                                .clip(RectangleShape)

                        ) {
                            Text(text = "Chấp nhận", color = Color.White)
                        }
                        Spacer(Modifier.weight(0.7f))
                        Button(
                            onClick = {
                                friendRequestViewModel.deleteFriendReq(Firebase.auth.currentUser!!.uid,uid)
                                friendSendViewModel.deleteFriendSend(uid, Firebase.auth.currentUser!!.uid)
                                notificationViewModel.deleteFriendNotification(Firebase.auth.currentUser!!.uid,uid)
                                actionTaken = "Đã từ chối kết bạn với $name" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .size(width = 132.dp, height = 37.dp)
                                .clip(RectangleShape)
                        ) {
                            Text(text = "Từ chối", color = colorResource(R.color.white))
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