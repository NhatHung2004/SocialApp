package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.domain.utils.toPrettyTime
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel,navController: NavController,friendViewModel: FriendViewModel,friendRequestViewModel: FriendRequestViewModel,friendSendViewModel: FriendSendViewModel){

    val context= LocalContext.current
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)

    val notification=notificationViewModel.notifications.collectAsState().value
    notificationViewModel.getNotifications(Firebase.auth.currentUser!!.uid)
    LazyColumn (modifier=Modifier.fillMaxSize()){
        item{
            FirstLine1()
            Spacer(Modifier.height(10.dp))
            Divider(
                color = colorResource(R.color.pink),
                thickness = 1.dp,
            )
        }
        item{
            Notification(notification,notificationViewModel,context, navController,notificationContents, friendViewModel , friendRequestViewModel , friendSendViewModel )
        }
    }
}
@Composable
fun FirstLine1(){
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.padding(top = 8.dp)){
        Text(
            text = "Thông báo", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 11.dp)
        )
        Spacer(modifier = Modifier.width(50.dp))

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Notification(nofInfo:Map<String,Any>?
                       ,notificationViewModel: NotificationViewModel
                       ,context: Context
                       ,navController: NavController
                       ,notificationContents: Array<String>
                       ,friendViewModel:FriendViewModel,
                       friendRequestViewModel:FriendRequestViewModel,
                       friendSendViewModel: FriendSendViewModel
){

    var actionTaken by remember { mutableStateOf<String?>(null) }
        if (nofInfo != null) {
            val nofInfoSorted = nofInfo.entries
                .map { it.key to (it.value as Map<String, Any>) }
                .sortedByDescending { (it.second["timestamp"] as Long) }
                .toMap()

            for ((index, entry) in nofInfoSorted.entries.withIndex()) {
                val nofData = entry.value as? Map<*, *>
                val uidUser = nofData?.get("uidUser") as? String
                val uidPost = nofData?.get("uidPost") as? String?
                val contentNof = nofData?.get("content") as? String
                val readState = nofData?.get("readState") as? String
                val timeStamp = nofData?.get("timestamp") as? Long
                var name by remember { mutableStateOf("") }
                var avatar by remember { mutableStateOf("") }
                LaunchedEffect(Unit) {
                    if (uidUser != null) {
                        val nameResutl = notificationViewModel.getName(uidUser)
                        name = nameResutl
                        val avatarResult = notificationViewModel.getAvatar(uidUser).toString()
                        avatar = avatarResult
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = {
                            if (readState == "notRead") {
                                notificationViewModel.updateReadState(entry.key, "read")
                            }
                            when (contentNof) {
                                notificationContents[0] -> {
                                    navController.navigate(Routes.ALL_FRIEND_REQ)
                                }

                                notificationContents[1] -> {
                                    navController.navigate(Routes.ALL_FRIEND)
                                    notificationViewModel.deleteFriendNotification(
                                        Firebase.auth.currentUser!!.uid,
                                        uidUser!!
                                    )
                                }

                                notificationContents[4] -> {
                                    navController.navigate("${Routes.POST_FOCUS}/${uidUser}/${uidPost}")
                                }

                                notificationContents[2] -> {
                                    navController.navigate("${Routes.POST_FOCUS}/${Firebase.auth.currentUser!!.uid}/${uidPost}")
                                }

                                notificationContents[3] -> {
                                    navController.navigate("${Routes.POST_FOCUS}/${Firebase.auth.currentUser!!.uid}/${uidPost}")
                                }
                            }
                            Toast.makeText(context, "Đã xem", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (readState == "read") MaterialTheme.colorScheme.background else colorResource(
                                R.color.pinkBlur
                            )
                        ),
                        modifier = Modifier.fillMaxWidth().background
                            (
                            color = if (readState == "read") MaterialTheme.colorScheme.background else colorResource(
                                R.color.pinkBlur
                            )
                        )

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(
                                    color = if (readState == "read") MaterialTheme.colorScheme.background else colorResource(
                                        R.color.pinkBlur
                                    )
                                )
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(avatar),
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(45.dp))
                            )
                            Spacer(Modifier.width(7.dp))
                            Column {
                                Row {
                                    Text(
                                        text = buildAnnotatedString {//gop nameNof và contentNof 1 text duy nhất
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            ) {
                                                append(name)
                                            }
                                            append(" ")
                                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                                append(contentNof)
                                            }
                                        },
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Row {
                                    if (timeStamp != null) {
                                        Text(
                                            text = timeStamp.toPrettyTime(),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                    Spacer(Modifier.weight(1f))
                                    if (contentNof == notificationContents[2] || contentNof == notificationContents[3] || contentNof == notificationContents[4]) {
                                        Button(
                                            onClick = {
                                                when (contentNof) {
                                                    notificationContents[4] -> {
                                                        if (uidUser != null && uidPost !== null) {
                                                            notificationViewModel.deletePostNotification(
                                                                Firebase.auth.currentUser!!.uid,
                                                                uidUser,
                                                                uidPost,
                                                                contentNof
                                                            )
                                                        }
                                                    }

                                                    notificationContents[2] -> {
                                                        if (uidUser != null && uidPost !== null) {
                                                            notificationViewModel.deletePostNotification(
                                                                Firebase.auth.currentUser!!.uid,
                                                                uidUser,
                                                                uidPost,
                                                                contentNof
                                                            )
                                                        }
                                                    }

                                                    notificationContents[3] -> {
                                                        if (uidUser != null && uidPost !== null) {
                                                            notificationViewModel.deletePostNotification(
                                                                Firebase.auth.currentUser!!.uid,
                                                                uidUser,
                                                                uidPost,
                                                                contentNof
                                                            )
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier.width(75.dp).height(25.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (readState == "notRead") colorResource(
                                                    R.color.pinkBlur
                                                )
                                                else MaterialTheme.colorScheme.background
                                            ),
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.close),
                                                contentDescription = "Back",
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.size(25.dp).offset(x = 5.dp)
                                            )
                                        }
                                    } else if (contentNof == notificationContents[0]) {
                                        Spacer(Modifier.width(5.dp))
                                        Row() {
                                            if (actionTaken == null) {
                                                Spacer(Modifier.height(10.dp))
                                                Button(
                                                    onClick = {
                                                        friendRequestViewModel.deleteFriendReq(
                                                            Firebase.auth.currentUser!!.uid,
                                                            uidUser.toString()
                                                        )
                                                        friendSendViewModel.deleteFriendSend(
                                                            uidUser.toString(),
                                                            Firebase.auth.currentUser!!.uid
                                                        )
                                                        friendViewModel.updateFriendToFirestore(
                                                            Firebase.auth.currentUser!!.uid,
                                                            uidUser.toString()
                                                        )
                                                        friendViewModel.updateFriendToFirestore(
                                                            uidUser.toString(),
                                                            Firebase.auth.currentUser!!.uid
                                                        )
                                                        notificationViewModel.updateNotificationToFireStore(
                                                            Firebase.auth.currentUser!!.uid,
                                                            "",
                                                            notificationContents[1],
                                                            "notRead",
                                                            uidUser.toString()
                                                        )
                                                        notificationViewModel.deleteFriendNotification(
                                                            Firebase.auth.currentUser!!.uid,
                                                            uidUser.toString()
                                                        )
                                                        actionTaken = "Đã chấp nhận "
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Gray
                                                    ),
                                                    modifier = Modifier
                                                        // Đặt kích thước cho nút
                                                        .border(
                                                            BorderStroke(
                                                                1.dp,
                                                                color = colorResource(R.color.pink)
                                                            ),
                                                            shape = RoundedCornerShape(15.dp)
                                                        )
                                                        .size(width = 75.dp, height = 37.dp)

                                                ) {
                                                    Image(
                                                        painter = painterResource(R.drawable.like),
                                                        contentDescription = "Back",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.size(23.dp)
                                                            .offset(x = 5.dp),
                                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                                    )
                                                }
                                                Spacer(Modifier.width(10.dp))
                                                Button(
                                                    onClick = {
                                                        friendRequestViewModel.deleteFriendReq(
                                                            Firebase.auth.currentUser!!.uid,
                                                            uidUser.toString()
                                                        )
                                                        friendSendViewModel.deleteFriendSend(
                                                            uidUser.toString(),
                                                            Firebase.auth.currentUser!!.uid
                                                        )
                                                        notificationViewModel.deleteFriendNotification(
                                                            Firebase.auth.currentUser!!.uid,
                                                            uidUser.toString()
                                                        )
                                                        actionTaken = "Đã từ chối "
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Gray
                                                    ),
                                                    modifier = Modifier
                                                        // Đặt kích thước cho nút
                                                        .border(
                                                            BorderStroke(
                                                                1.dp,
                                                                color = colorResource(R.color.pink)
                                                            ),
                                                            shape = RoundedCornerShape(15.dp)
                                                        )
                                                        .size(width = 75.dp, height = 37.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(R.drawable.dontlike),
                                                        contentDescription = "Back",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.size(23.dp)
                                                            .offset(x = 5.dp),
                                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                                    )
                                                }
                                            } else {
                                                Spacer(Modifier.height(2.dp))
                                                Text(
                                                    text = actionTaken!!,
                                                    color = Color.Gray,
                                                    fontSize = 15.sp,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
}