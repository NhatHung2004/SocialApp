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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Friend
import com.example.social.db.userPostDataProvider
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllFriend(friendViewModel: FriendViewModel, friendRequestViewModel: FriendRequestViewModel, friendSendViewModel: FriendSendViewModel){
    var text by remember { mutableStateOf("") }
    val context= LocalContext.current
    val friends=friendViewModel.friends.collectAsState().value
    val friendRequests=friendRequestViewModel.friendRequests.collectAsState().value
    val friendSends=friendSendViewModel.friendSends.collectAsState().value

    val userInfoList = friendViewModel.userInfo.collectAsState().value

    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)
    friendRequestViewModel.getFriendRequests(Firebase.auth.currentUser!!.uid)
    friendSendViewModel.getFriendSends(Firebase.auth.currentUser!!.uid)

    val userIds = mutableListOf<Friend>()
    val userIdRequests = mutableListOf<Friend>()
    val userIdSends = mutableListOf<Friend>()

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
                friendViewModel.getFriendInfo(userIds.map { it.uid })
                if(text.isEmpty()) {
                    userInfoList.forEach{userInfo ->
                        val friendData=userIds.find{it.uid==userInfo["uid"]}
                        if (friendData != null) {
                            ListFriend(
                                userInfo,
                                userIds.map { it.uid },
                                userIdRequests.map { it.uid },
                                userIdSends.map { it.uid },
                                friendData.timestamp,
                                friendViewModel,
                                friendRequestViewModel,
                                friendSendViewModel
                            )
                        }
                    }
                }
                else{
                    userInfoList.forEach{userInfo ->
                        if ((userInfo["lastname"] as? String)?.lowercase()?.contains(text.lowercase()) == true) {
                            val friendData=userIds.find{it.uid==userInfo["uid"]}
                            if (friendData != null) {
                                ListFriend(
                                    userInfo,
                                    userIds.map { it.uid },
                                    userIdRequests.map { it.uid },
                                    userIdSends.map { it.uid },
                                    friendData.timestamp,
                                    friendViewModel,
                                    friendRequestViewModel,
                                    friendSendViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListFriend(friend: Map<String,Any>,userIdFriends:List<String>,userIdRequests:List<String>,userIdSends:List<String>,time:Long,friendViewModel: FriendViewModel
               ,friendRequestViewModel: FriendRequestViewModel
               ,friendSendViewModel: FriendSendViewModel
) {
    val isPressed = remember { mutableStateOf(false) }
    var buttonBottomSheetState by remember { mutableStateOf<Boolean?>(false) }
    val showBottomSheet = remember { mutableStateOf(false) }

    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
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
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    modifier = Modifier.offset(y = 10.dp)
                )
                Spacer(Modifier.height(11.dp))
                if (!isPressed.value) {
                    Text(text = "100 bạn chung")
                } else {
                    if (buttonBottomSheetState == false) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Đã hủy kết bạn", modifier = Modifier.padding(top = 5.dp))
                            Spacer(Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    friendRequestViewModel.updateFriendRequestToFirestore(
                                        friend["uid"].toString(), Firebase.auth.currentUser!!.uid
                                    )
                                    friendSendViewModel.updateFriendSendToFirestore(
                                        Firebase.auth.currentUser!!.uid, friend["uid"].toString()
                                    )
                                    buttonBottomSheetState = true
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
                    } else {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Đã gửi yêu cầu ", modifier = Modifier.padding(top = 5.dp))
                            Spacer(Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    friendRequestViewModel.deleteFriendReq(
                                        friend["uid"].toString(), Firebase.auth.currentUser!!.uid
                                    )

                                    friendSendViewModel.deleteFriendSend(
                                        Firebase.auth.currentUser!!.uid, friend["uid"].toString()
                                    )
                                    buttonBottomSheetState = true
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
        Spacer(Modifier.height(20.dp))
    }
    if (showBottomSheet.value) {
        FriendBottomSheet(
            friend, userIdRequests,time,
            isPressed,
            friendViewModel,
            onDismiss = { showBottomSheet.value = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldFriendSearch( text: String, onTextChange: (String) -> Unit){
    TextField(
        value = text,
        onValueChange = { newText -> onTextChange(newText) },
        placeholder = { Text("Tìm kiếm bạn bè ") }, // Sử dụng placeholder
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.searching),
                contentDescription = "option Icon",
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Send
        ),
//        keyboardActions = KeyboardActions(
//            onSend = {
//                Toast.makeText(context,"Đã gửi", Toast.LENGTH_SHORT).show()
//                text=""
//            }
//        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}
