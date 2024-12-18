package com.example.social.presentation.ui


import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Post
import com.example.social.domain.utils.toPrettyTime
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileFriendScreen(navController: NavController, uid: String, profileViewModel: ProfileViewModel,
                        postViewModel: PostViewModel, friendViewModel: FriendViewModel, friendRequestViewModel: FriendRequestViewModel,commentViewModel: CommentViewModel,notificationViewModel: NotificationViewModel
) {
    val context= LocalContext.current
    val comments = commentViewModel.comments.collectAsState().value

    val posts = postViewModel.posts.collectAsState().value
    postViewModel.getPosts(uid)

//    val friends=profileOfFriendViewModel.friends.collectAsState().value
//    profileOfFriendViewModel.getFriendOfFriend(uid)

//    profileOfFriendViewModel.getUserInfoFromId(uid)

    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    var background by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val firstnameResult = profileViewModel.getFirstname(uid)
        if (firstnameResult != null)
            first = firstnameResult
        val lastnameResult = profileViewModel.getLastname(uid)
        if (lastnameResult != null)
            last = lastnameResult
        val avatarResult = profileViewModel.getAvatar(uid)
        if (avatarResult != null) {
            avatar = avatarResult
        }
        val backgroundAvatarResult = profileViewModel.getBackground(uid)
        if (backgroundAvatarResult != null) {
            background = backgroundAvatarResult
        }
    }


    var friends by remember { mutableStateOf<Map<String, Any>?>(null) }
    var friendRequests by remember { mutableStateOf<Map<String, Any>?>(null) }
    LaunchedEffect(Unit) {
        val fs = friendViewModel.getFriend(uid)
        val fr = friendRequestViewModel.getFriendRequest(uid)
        friends = fs
        friendRequests=fr
    }


    Column(
        modifier = Modifier
            .fillMaxSize(), // Đảm bảo Column chiếm toàn bộ kích thước có sẵn
        verticalArrangement = Arrangement.SpaceBetween // Căn đều giữa các thành phần
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn() {
                item{
                    Row(){
                        Button(
                            onClick = {navController.popBackStack()},
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
                        Spacer(Modifier.width(50.dp))
                        Text(text= "$first $last", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground
                            , modifier = Modifier.padding(top=5.dp), fontSize = 25.sp)
                    }
                }
                item {
                    //, imageAvatar, imageBackground
                    FirstlineFriend5(
                        navController,
                        avatar,
                        background,
                        first,
                        last,
                        posts,
                        friends,
                        friendRequests
                    )
                }
                item {
                    Spacer(Modifier.height(15.dp))
                    Column( modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),) {
                        val friendsList = friends?.toList() ?: emptyList()
                        Log.i("i", friendsList.toString())
                        friendsList.chunked(3).forEach { chunk ->
                            Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                                chunk.forEach { (uid, friend) ->
                                    val friendData = friend as? Map<*, *>
                                    var first by remember { mutableStateOf("") }
                                    var last by remember { mutableStateOf("") }
                                    var avatar by remember { mutableStateOf("") }
                                    LaunchedEffect(Unit) {
                                        val firstnameResult = friendViewModel.getFirstname(
                                            friendData?.get("uid") as String)
                                        if (firstnameResult != null)
                                            first = firstnameResult
                                        val lastnameResult = friendViewModel.getLastname(friendData["uid"] as String)
                                        if (lastnameResult != null)
                                            last = lastnameResult
                                        val avatarResult = friendViewModel.getAvatar(friendData["uid"] as String)
                                        if (avatarResult != null) {
                                            avatar = avatarResult
                                        }
                                    }

                                    // Hiển thị thông tin bạn bè
                                    FriendLineOfFriend(
                                        navController = navController,
                                        "$first $last",
                                        avatar
                                    )
                                }
                            }
                        }
//                        for ((index, entry) in friends?.entries?.withIndex()!!) {
//                            val friendData = entry.value as? Map<*, *>
//
//                        }
                    }
                }
                item{
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {navController.navigate("${Routes.ALL_FRIEND_OF_FRIEND}/$uid")},
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
                item {
                    Spacer(Modifier.height(10.dp))
                    if (posts != null) {
                        for ((index, entry) in posts.entries.withIndex()) {
                            val postData = entry.value as? Map<*, *>
                            val imageUris = postData?.get("imageUris") as List<String>
                            val liked = postData["liked"] as List<String>
                            val report=postData["report"] as String
                            val content = postData["content"]
                            val timestamp = postData["timestamp"] as Long
                            val id = postData["id"]
                            val userID = postData["userID"]
                            val post = Post(id.toString(), userID.toString(), content.toString(),
                                timestamp, imageUris, liked,report)
                            val like = post.liked.contains(Firebase.auth.currentUser!!.uid)
                            if(like) {
                                FriendPost(post, avatar, "$first $last", timestamp.toPrettyTime(),
                                    commentViewModel, postViewModel,notificationViewModel,context, comments, true)
                            } else {
                                FriendPost(post, avatar, "$first $last", timestamp.toPrettyTime(),
                                    commentViewModel, postViewModel,notificationViewModel,context, comments, false)
                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FirstlineFriend5(
    navController: NavController, imageAvatar: String?, imageBackground: String?,
    firstname: String, lastname: String,posts: Map<String,Any>?,friends: Map<String,Any>?,friendRequests: Map<String,Any>?
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
        ) {
            GetNenHinhDaiDien(imageBackground)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = 1.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GetHinhDaiDienProfile(imageAvatar)
                    Spacer(Modifier.width(15.dp))
                    Box(modifier = Modifier.offset(y = 25.dp)) {
                        Row() {
                            Column {
                                if (posts != null) {
                                    Text(text = posts.size.toString(), modifier = Modifier.padding(start = 25.dp))
                                }
                                Text(text = "Bài viết")
                            }
                            Spacer(Modifier.width(20.dp))
                            Column {
                                if (friends != null) {
                                    Text(text = friends.size.toString(), modifier = Modifier.padding(start = 25.dp))
                                }
                                Text(text = "Bạn bè")
                            }
                            Spacer(Modifier.width(20.dp))
                            Column {
                                if (friendRequests != null) {
                                    Text(text = friendRequests.size.toString(), modifier = Modifier.padding(start = 25.dp))
                                }
                                Text(text = "Theo dõi")
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = "$firstname $lastname", modifier = Modifier.padding(start = 15.dp),
                fontWeight = FontWeight.ExtraBold, fontSize = 20.sp
            )
            Spacer(Modifier.weight(1f))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendPost(
    post: Post,
    imageAvatar: String?,
    name: String,
    time: String,
    commentViewModel: CommentViewModel,
    postViewModel: PostViewModel,
    notificationViewModel: NotificationViewModel,
    context: Context,
    comments: Map<String, Any>?,
    like: Boolean) {
    val showBottomSheet = remember { mutableStateOf(false) }
    var isToggled by remember { mutableStateOf(like) }

    val notificationContents =context.resources.getStringArray(R.array.notification_contents)

    Column(modifier=Modifier.fillMaxSize()){
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)){
            Image(
                painter = rememberAsyncImagePainter(imageAvatar),
                contentDescription = "Circular Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(41.dp) // Kích thước ảnh tròn
                    .clip(CircleShape) // Cắt ảnh thành hình tròn
                    .border(1.dp, colorResource(R.color.pinkBlur), CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                )
                Text(text = time)
            }
        }
        Row (modifier = Modifier.fillMaxWidth()){
            Text(text = post.content, modifier = Modifier.padding(start = 10.dp))
        }
        // ảnh post
        LazyRow(
            modifier = Modifier.fillMaxWidth(), // Chiều rộng đầy đủ
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các hình ảnh
        ) {
            items(post.imageUris) { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .height(400.dp)
                        .width(385.dp)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Image(
                painter= painterResource(R.drawable.like1),
                contentDescription="option",
                modifier = Modifier.size(16.dp)
            )
            Image(
                painter= painterResource(R.drawable.heart),
                contentDescription="option",
                modifier = Modifier.size(16.dp)
            )
            //Text(text=userPosts.count.toString()+userPosts.quantity) text hien thi so luong tim va like
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                isToggled = !isToggled
                postViewModel.updateLiked(post.id, post.userID, Firebase.auth.currentUser!!.uid)
                if(post.userID!=Firebase.auth.currentUser!!.uid) {
                    notificationViewModel.updateNotificationToFireStore(
                        Firebase.auth.currentUser!!.uid,
                        post.id,
                        notificationContents[2],
                        "notRead",
                        post.userID
                    )
                }
            },
                modifier = Modifier.padding(start = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                if(isToggled) {
                    Image(
                        painter = painterResource(R.drawable.like1),
                        contentDescription = "option",
                        modifier = Modifier.size(16.dp)
                    )
                }
                else{
                    Image(
                        painter = painterResource(R.drawable.like),
                        contentDescription = "option",
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(11.dp))
                if(isToggled){
                    Text(text = "Thích",color=Color.Blue, fontWeight = FontWeight.Bold)
                }
                else{
                    Text(text = "Thích",color= MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.weight(1f))

            Button(onClick={showBottomSheet.value = true},modifier = Modifier.padding(end = 5.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.White)) {
                Image(
                    painter = painterResource(R.drawable.speechbubble),
                    contentDescription = "option",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(11.dp))
                Text(text = "Bình luận",color=Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
    if (showBottomSheet.value) {
        if (imageAvatar != null) {
            cmtPart(onDismiss = { showBottomSheet.value = false }, name, post.id,post.userID,imageAvatar,
                commentViewModel ,notificationViewModel)
        } // Gọi hàm `cmtPart` và ẩn khi hoàn tất
    }
}

@Composable
fun FriendLineOfFriend(navController: NavController, name: String, avatarUri: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa các phần tử trong cột
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp)) // Thêm spacer ở đây để đẩy hình ảnh xuống
            Image(
                painter = rememberAsyncImagePainter(avatarUri),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 92.dp, height = 51.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(2.dp))
            )
        }
        Text(text = name)
    }
}

