package com.example.social.presentation.ui

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.data.model.Friend
import com.example.social.data.model.Post
import com.example.social.db.userAvatars
import com.example.social.db.userPostDataProvider
import com.example.social.db.userPosts
import com.example.social.domain.utils.convertToTime
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, friendViewModel: FriendViewModel,
               profileViewModel: ProfileViewModel, postViewModel: PostViewModel,
               commentViewModel: CommentViewModel,notificationViewModel: NotificationViewModel
){
    val context= LocalContext.current
    val friends = friendViewModel.friends.collectAsState().value
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)
    postViewModel.getAllPosts()
    val allPosts = postViewModel.allPosts.collectAsState().value
    val comments = commentViewModel.comments.collectAsState().value

    profileViewModel.getUserInfo()

    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value
    val lastname = profileViewModel.lastname.collectAsState().value

    val userInfoList = friendViewModel.userInfo.collectAsState().value
    val userIds = mutableListOf<Friend>()

    if(friends != null) {
        for ((index, entry) in friends.entries.withIndex()) {
            val friendData= entry.value as? Map<*, *>
            val userId = friendData?.get("uid") as? String
            val timestamp = friendData?.get("timestamp") as Long
            if (userId != null) {
                userIds.add(Friend(userId,timestamp))
            }
        }
    }


    LazyColumn (
        modifier=Modifier.fillMaxWidth(),
    ) {
        item() {
            LazyRow {
                item {
                    Column(
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Box {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Blue
                                ),
                                modifier = Modifier.size(20.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .zIndex(1f),
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "add",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)

                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color.White) // Nền màu trắng
                                    .border(1.dp, Color.Black, RoundedCornerShape(50.dp))
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(imageAvatar),
                                    contentDescription = "avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Text(text = lastname)
                    }
                    Spacer(Modifier.width(16.dp))
                }
                item {
                    friendViewModel.getFriendInfo(userIds.map { it.uid })
                    userInfoList.forEach { userInfo ->
                        FriendList(userInfo,navController)
                    }
                }
            }
        }
        item {
            Spacer(Modifier.height(10.dp))
            if (allPosts != null) {
                for (posts in allPosts.reversed()) {
                    for ((index, entry) in posts.entries.withIndex()) {
                        val postData = entry.value as? Map<*, *>
                        val imageUris = postData?.get("imageUris") as List<String>
                        val liked = postData["liked"] as List<String>
                        val report = postData["report"]
                        val content = postData["content"]
                        val timestamp = postData["timestamp"] as Long
                        val id = postData["id"]
                        val userIDPost = postData["userID"]
                        var first by remember { mutableStateOf("") }
                        var last by remember { mutableStateOf("") }
                        var avatar by remember { mutableStateOf("") }
                        LaunchedEffect(Unit) {
                            val firstnameResult = postViewModel.getFirstname(userIDPost.toString())
                            if (firstnameResult != null)
                                first = firstnameResult
                            val lastnameResult = postViewModel.getLastname(userIDPost.toString())
                            if (lastnameResult != null)
                                last = lastnameResult
                            val avatarResult = postViewModel.getAvatar(userIDPost.toString())
                            if (avatarResult != null) {
                                avatar = avatarResult
                            }
                        }
                        val post = Post(
                            id.toString(), userIDPost.toString(), content.toString(),
                            timestamp, imageUris, liked, report.toString()
                        )
                        val like = post.liked.contains(Firebase.auth.currentUser!!.uid)
                        if (userIds.contains(userIDPost) || userIDPost == Firebase.auth.currentUser!!.uid) {
                        if (like) {
                            SelfPost(
                                post,
                                avatar,
                                "$first $last",
                                convertToTime(timestamp),
                                commentViewModel,
                                postViewModel,
                                notificationViewModel,
                                context,
                                comments,
                                true
                            )
                        } else {
                            SelfPost(
                                post,
                                avatar,
                                "$first $last",
                                convertToTime(timestamp),
                                commentViewModel,
                                postViewModel,
                                notificationViewModel,
                                context,
                                comments,
                                false
                            )
                        }
                    }

                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FriendList(friend: Map<String,Any>,navController: NavController
) {
    val name = "${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val status=friend["status"]
    val userId=friend["uid"]
    Column(horizontalAlignment = Alignment.CenterHorizontally,) {

        Box(modifier = Modifier.clickable {
            navController.navigate("${Routes.FRIEND_PROFILE}/$userId") }) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (status == "online") Color.Green else Color.Red
                    )
                    .align(Alignment.BottomEnd)
                    .zIndex(1f)
            ) {}
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White) // Nền màu trắng
                    .border(1.dp, Color.Black, RoundedCornerShape(50.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUri),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        Spacer(Modifier.height(5.dp))
        Text(text = name)
    }
    Spacer(Modifier.width(16.dp))
}
