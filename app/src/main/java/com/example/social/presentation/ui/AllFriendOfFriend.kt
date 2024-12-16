package com.example.social.presentation.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Friend
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.example.social.presentation.viewmodel.ProfileOfFriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.checkerframework.checker.units.qual.C
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllFriendOfFriend(uid:String,friendViewModel: FriendViewModel
                      ,friendRequestViewModel: FriendRequestViewModel
                      ,friendSendViewModel: FriendSendViewModel
                      ,profileOfFriendViewModel: ProfileOfFriendViewModel,notificationViewModel: NotificationViewModel){
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)

    val friendsOfFriend=profileOfFriendViewModel.friends.collectAsState().value
    val friendOfCurrentUser=friendViewModel.friends.collectAsState().value
    val friendRequests=friendRequestViewModel.friendRequests.collectAsState().value
    val friendSends=friendSendViewModel.friendSends.collectAsState().value
    val userInfoList = profileOfFriendViewModel.userInfo.collectAsState().value

    profileOfFriendViewModel.getFriendOfFriend(uid)
    friendRequestViewModel.getFriendRequests(Firebase.auth.currentUser!!.uid)
    friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)

    val userIdsFriendOfFriend = mutableListOf<Friend>()
    val userIdRequests = mutableListOf<Friend>()
    val userIdSends = mutableListOf<Friend>()
    val userIds = mutableListOf<Friend>()

    val isPressedStateFalse = remember { mutableStateOf(false) }
    val isPressedStateTrue = remember { mutableStateOf(true) }

    if(friendOfCurrentUser!=null) {
        for ((index, entry) in friendOfCurrentUser.entries.withIndex()) {
            val friendData = entry.value as? Map<*, *>
            val userId = friendData?.get("uid") as? String
            val timestamp = friendData?.get("timestamp") as Long
            if (userId != null) {
                userIds.add(Friend(userId,timestamp))
            }
        }
    }

    if(friendsOfFriend!=null) {
        for ((index, entry) in friendsOfFriend.entries.withIndex()) {
            val friendData = entry.value as? Map<*, *>
            val userId = friendData?.get("uid") as? String
            val timestamp = friendData?.get("timestamp") as Long
            if (userId != null) {
                userIdsFriendOfFriend.add(Friend(userId,timestamp))
            }
        }
    }

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

    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
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
            Text(text="Bạn bè",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                // Đặt kích thước cho nút
                .border(
                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                    shape = RoundedCornerShape(15.dp)
                )
                .fillMaxWidth().height(52.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            TextFieldFriendSearch(text=text){
                text=it
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = userIdsFriendOfFriend.size.toString(), fontSize = 20.sp,
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
                profileOfFriendViewModel.getFriendOfFriendInfo(userIdsFriendOfFriend.map { it.uid })
                if(text.isEmpty()) {
                    userInfoList.forEachIndexed { index,userInfo ->
                        val currentDate = Instant.ofEpochMilli(userIdsFriendOfFriend[index].timestamp)
                            .atZone(ZoneId.systemDefault()) // Lấy múi giờ hệ thống
                            .toLocalDate()
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        val formattedDate = currentDate.format(formatter)
                        if(userIds.contains(userInfo["uid"])) {
                            ListFriend(
                                userInfo,null,
                                userIdsFriendOfFriend.map { it.uid },
                                friendViewModel,
                                friendRequestViewModel,
                                friendSendViewModel
                            )
                        }
                        else if(userIdSends.map { it.uid }.contains(userInfo["uid"])){
                            ListFriendOfFriend(userInfo,friendRequestViewModel,friendSendViewModel,isPressedStateTrue)
                        }
                        else if(userIdRequests.map { it.uid }.contains(userInfo["uid"])){
                            FriendReqDisplay(userInfo,null,friendViewModel, friendRequestViewModel, friendSendViewModel, notificationViewModel ,notificationContents)
                        }
                        else{
                            ListFriendOfFriend(userInfo,friendRequestViewModel,friendSendViewModel,isPressedStateFalse)
                        }
                    }
                }
                else{
                    userInfoList.forEach { userInfo ->
                        if ((userInfo["lastname"] as? String)?.lowercase()?.contains(text.lowercase()) == true) {
                            if(userIds.contains(userInfo["uid"])) {
                                ListFriend(
                                    userInfo,null,
                                    userIdsFriendOfFriend.map { it.uid },
                                    friendViewModel,
                                    friendRequestViewModel,
                                    friendSendViewModel
                                )
                            }
                            else if(userIdSends.contains(userInfo["uid"])){
                                ListFriendOfFriend(userInfo,friendRequestViewModel,friendSendViewModel,isPressedStateTrue)
                            }
                            else{
                                ListFriendOfFriend(userInfo,friendRequestViewModel,friendSendViewModel,isPressedStateFalse)
                            }
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun ListFriendOfFriend(friend: Map<String,Any>
                       ,friendRequestViewModel: FriendRequestViewModel
                       ,friendSendViewModel: FriendSendViewModel,isPressed: MutableState<Boolean>
) {
    val showBottomSheet = remember { mutableStateOf(false) }

    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val uid = friend["uid"]
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberAsyncImagePainter(avatarUri),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(35.dp))
        )
        Spacer(Modifier.width(10.dp))
        if (friend["uid"] == Firebase.auth.currentUser!!.uid) {
            Text(
                text = name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.offset(y = 10.dp)
            )
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    modifier = Modifier.offset(y = 10.dp)
                )
                Spacer(Modifier.height(5.dp))
                if (!isPressed.value) {
                    Spacer(Modifier.height(5.dp))
                    Button(
                        onClick = {
                            friendRequestViewModel.updateFriendRequestToFirestore(
                                friend["uid"].toString(), Firebase.auth.currentUser!!.uid
                            )
                            friendSendViewModel.updateFriendSendToFirestore(
                                Firebase.auth.currentUser!!.uid, friend["uid"].toString()
                            )
                            isPressed.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.pink)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            // Đặt kích thước cho nút
                            .fillMaxWidth().height(32.dp)
                            .clip(RectangleShape)
                    ) {
                        Text(text = "Thêm bạn", color = Color.White)
                    }
                } else {
                    Spacer(Modifier.height(5.dp))
                    Button(
                        onClick = {
                            friendRequestViewModel.deleteFriendReq(
                                friend["uid"].toString(), Firebase.auth.currentUser!!.uid
                            )
                            friendSendViewModel.deleteFriendSend(
                                Firebase.auth.currentUser!!.uid, friend["uid"].toString()
                            )
                            isPressed.value = false
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
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { showBottomSheet.value = true },
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
    }
    Spacer(Modifier.height(20.dp))
}

