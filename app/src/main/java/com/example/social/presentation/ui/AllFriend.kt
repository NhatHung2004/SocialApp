package com.example.social.presentation.ui

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.db.userPostDataProvider
import com.example.social.presentation.viewmodel.FriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AllFriend(friendViewModel: FriendViewModel = viewModel()){
    val friends=friendViewModel.friends.collectAsState().value
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid,"friends")

    val userIds = remember(friends) {
        friends?.mapNotNull { (key, value) ->
            (value as? Map<*, *>)?.get("uid")?.toString()
        }.orEmpty()
    }

    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Bạn bè",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                // Đặt kích thước cho nút
                .border(
                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                    shape = RoundedCornerShape(15.dp)
                )
                .size(width =350.dp, height = 37.dp)
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
                        text = "Tìm kiếm bạn bè",
                        color = colorResource(R.color.pink),
                        fontSize = 20.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = userIds.size.toString(), fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color= colorResource(R.color.pink)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "người bạn", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn (modifier=Modifier.fillMaxSize().padding(start=6.dp)){
            item {
                val userInfoList = friendViewModel.userInfo.collectAsState().value
                if (userInfoList.isEmpty() && userIds.isNotEmpty()) {
                    friendViewModel.getFriendInfo(userIds)
                }
                userInfoList.forEachIndexed { index, userInfo ->
                    val userId = userIds.getOrNull(index)
                    if (userId != null) {
                        ListFriend(userInfo,userId)
                    }
                }
            }
        }
    }
}
@Composable
fun ListFriend(friend: Map<String,Any>,userId:String,friendViewModel: FriendViewModel= viewModel()){
    var isPressed = remember { mutableStateOf(false) }
    var ispressedAddcc by remember { mutableStateOf<Boolean?>(false) }
    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())

    val showBottomSheet = remember { mutableStateOf(false) }

    Column (
        modifier=Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            GetHinhDaiDienAllFriend(avatarUri)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    modifier = Modifier.offset(y = 10.dp)
                )
                Spacer(Modifier.height(11.dp))
                if(!isPressed.value) {
                    Text(text = "100 bạn chung")
                }else
                {
                    if(ispressedAddcc==false) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Đã hủy kết bạn", modifier = Modifier.padding(top = 5.dp))
                            Spacer(Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    friendViewModel.updateFriendToFireStore("friendReqs", friend["uid"].toString(), Firebase.auth.currentUser!!.uid)
                                    friendViewModel.updateFriendToFireStore("friendSends",  Firebase.auth.currentUser!!.uid,friend["uid"].toString())
                                    ispressedAddcc=true
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
                        }
                    }
                    else{
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Đã gửi yêu cầu ", modifier = Modifier.padding(top = 5.dp))
                            Spacer(Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    friendViewModel.deleteFriendReq("friendReqs", userId, Firebase.auth.currentUser!!.uid)
                                    friendViewModel.deleteFriendReq("friendSends",  Firebase.auth.currentUser!!.uid,userId)
                                    ispressedAddcc=false
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
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    showBottomSheet.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            )
            {
                Image(
                    painter = painterResource(R.drawable.meatballsmenuc),
                    contentDescription = "option Icon",
                    modifier = Modifier
                        .size(24.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
            }

        }
        Spacer(Modifier.height(20.dp))
    }
    if(showBottomSheet.value){
        FriendBottomSheet(friend, isPressed, onDismiss = {showBottomSheet.value=false})
    }
}
@Composable
fun GetHinhDaiDienAllFriend(img2: Uri){
    Image(
        painter= rememberAsyncImagePainter(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}