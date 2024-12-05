package com.example.social.presentation.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.StartFriendSuggestViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun FriendScreen(navController: NavController, startFriendSuggest: StartFriendSuggestViewModel = viewModel(), friendViewModel: FriendViewModel = viewModel()
) {
    val users = startFriendSuggest.allUsers.collectAsState().value
    val friendReqs=friendViewModel.friendReqs.collectAsState().value
    val friendSends=friendViewModel.friendSends.collectAsState().value
    val friends=friendViewModel.friends.collectAsState().value
    val context= LocalContext.current

    friendViewModel.getFriendReqs(Firebase.auth.currentUser!!.uid, "friendReqs")
    friendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid, "friendSends")
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid,"friends")

    val userIdReqs = remember(friendReqs) {
        friendReqs?.mapNotNull { (key, value) ->
            (value as? Map<*, *>)?.get("uid")?.toString()
        }.orEmpty()
    }

    val userIdSends = remember(friendSends) {
        friendSends?.mapNotNull { (key, value) ->
            (value as? Map<*, *>)?.get("uid")?.toString()
        }.orEmpty()
    }

    val userIds = remember(friends) {
        friends?.mapNotNull { (key, value) ->
            (value as? Map<*, *>)?.get("uid")?.toString()
        }.orEmpty()
    }

    LazyColumn( modifier=Modifier.fillMaxSize()) {
        item{
            FirstLine(navController)
        }
        item{
            Text(text="Lời mời kết bạn"
                , color=colorResource(R.color.pink)
                , fontWeight = FontWeight.ExtraBold
                ,modifier=Modifier.padding(start = 10.dp))
            Spacer(Modifier.height(5.dp))


            val userInfoList = friendViewModel.userInfo.collectAsState().value
            if (userInfoList.isEmpty() && userIdReqs.isNotEmpty()) {
                friendViewModel.getFriendInfo(userIdReqs)
            }
            userInfoList.forEachIndexed{index,userInfo->
                val userId = userIdReqs.getOrNull(index)
                if (userId != null) {
                    FriendReqDisplay(userInfo,userId,navController)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { navController.navigate(Routes.ALL_FRIEND_REQ) },
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
            if(user["uid"]!=Firebase.auth.currentUser!!.uid  && user["uid"]!in userIdReqs && user["uid"]!in userIdSends && user["uid"]!in userIds) {
                RecommendFriendTab(user, context)
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

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    // Đặt kích thước cho nút
                    .border(
                        BorderStroke(1.dp, color = colorResource(R.color.pink)),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .size(width = 200.dp, height = 37.dp)
            ) {
                // Sử dụng Box để căn giữa nội dung
                Box(
                    // Chiếm toàn bộ không gian của nút
                    contentAlignment = Alignment.Center // Căn giữa nội dung
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.searching),
                            contentDescription = "option Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Tìm kiếm",
                            color = colorResource(R.color.pink),
                            fontSize = 20.sp,
                        )
                    }
                }
            }

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
fun RecommendFriendTab(user: Map<String, Any>, context: Context,friendViewModel: FriendViewModel = viewModel()) {
    var ispressed by remember { mutableStateOf<Boolean?>(false) }

    val name = "${user["firstname"]} ${user["lastname"]}"
    val avatarUri=Uri.parse(user["avatar"].toString())
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
                            friendViewModel.updateFriendToFireStore("friendReqs", user["uid"].toString(), Firebase.auth.currentUser!!.uid)
                            friendViewModel.updateFriendToFireStore("friendSends", Firebase.auth.currentUser!!.uid, user["uid"].toString(),)
                            //Database.addReqFreind(userId, Firebase.auth.currentUser!!.uid)
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
                                friendViewModel.deleteFriendReq(
                                    "friendSends", Firebase.auth.currentUser!!.uid,
                                    user["uid"].toString()
                                )
                                friendViewModel.deleteFriendReq(
                                    "friendReqs",
                                    user["uid"].toString(),
                                    Firebase.auth.currentUser!!.uid
                                )
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
                Spacer(Modifier.weight(0.7f))
                Button(onClick={ Toast.makeText(context, "Đã từ chối kết bạn vơi $name", Toast.LENGTH_SHORT).show()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.lightGrey)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        // Đặt kích thước cho nút
                        .size(width = 132.dp, height = 37.dp)
                        .clip(RectangleShape)
                ){
                    Text(text="Gỡ",color= colorResource(R.color.pink))
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun FriendSendDisplay(friend: Map<String,Any>,userId:String,friendViewModel: FriendViewModel= viewModel()) {
    var ispressed by remember { mutableStateOf<Boolean?>(false) }

    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
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
                            friendViewModel.deleteFriendReq("friendSends",Firebase.auth.currentUser!!.uid,userId)
                            friendViewModel.deleteFriendReq("friendReqs",userId,Firebase.auth.currentUser!!.uid)
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
fun FriendReqDisplay(friend: Map<String,Any>,userId:String, navController: NavController,friendViewModel: FriendViewModel= viewModel()) {
    var actionTaken by remember { mutableStateOf<String?>(null) }

    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    Row(modifier = Modifier.fillMaxWidth()) {
        Box() {
            AsyncImage(
                model = avatarUri,
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
                        val context = LocalContext.current
                        Button(
                            onClick = { friendViewModel.deleteFriendReq("friendReqs",Firebase.auth.currentUser!!.uid,userId)
                                friendViewModel.updateFriendToFireStore("friends",Firebase.auth.currentUser!!.uid,userId)
                                friendViewModel.updateFriendToFireStore("friends",userId,Firebase.auth.currentUser!!.uid)
                                friendViewModel.deleteFriendReq("friendSends",userId,Firebase.auth.currentUser!!.uid)
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
                            onClick = {friendViewModel.deleteFriendReq("friendReqs",Firebase.auth.currentUser!!.uid,userId)
                                friendViewModel.deleteFriendReq("friendSends",userId,Firebase.auth.currentUser!!.uid)
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