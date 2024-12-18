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
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.example.social.data.model.Friend
import com.example.social.data.model.Post
import com.example.social.domain.utils.SetText
import com.example.social.domain.utils.convertToTime
import com.example.social.domain.utils.setText2
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.example.social.presentation.viewmodel.ThemeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(navController: NavController, navControllerTab: NavController,
                  authViewModel: AuthViewModel,
                  commentViewModel: CommentViewModel,
                  postViewModel: PostViewModel,
                  profileViewModel: ProfileViewModel,
                  friendViewModel: FriendViewModel,
                  notificationViewModel: NotificationViewModel,
                  friendRequestViewModel: FriendRequestViewModel,
                  themeViewModel: ThemeViewModel
) {
    val context= LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    postViewModel.getPosts(Firebase.auth.currentUser!!.uid)
    profileViewModel.getUserInfo()

    val userInfoList = friendViewModel.userInfo.collectAsState().value

    val posts = postViewModel.posts.collectAsState().value
    val comments = commentViewModel.comments.collectAsState().value
    postViewModel.getPosts(Firebase.auth.currentUser!!.uid)

    val friends=friendViewModel.friends.collectAsState().value
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)

    val imageBackground = profileViewModel.imageBackgroundUri.collectAsState().value
    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value

    val firstname = profileViewModel.firstname.collectAsState().value
    val lastname = profileViewModel.lastname.collectAsState().value

    val showBottomSheet = remember { mutableStateOf(false) }

    // Theo dõi trạng thái sáng/tối từ ViewModel
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)

    // Lắng nghe sự thay đổi từ Switch
    LaunchedEffect(isDarkTheme) {
        themeViewModel.toggleTheme(isDarkTheme)
    }

    val userIds = mutableListOf<Friend>()
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

    Surface(
        modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ màn hình
        color = MaterialTheme.colorScheme.background, // Màu nền
        contentColor = MaterialTheme.colorScheme.onBackground // Màu chữ
    ){

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
            ){
                LazyColumn() {
                    item {
                        //, imageAvatar, imageBackground
                        Firstline5(navControllerTab,
                            imageAvatar,
                            imageBackground,
                            firstname,
                            lastname,
                            posts
                        )
                    }
                    item {
                        Spacer(Modifier.height(15.dp))
                        friendViewModel.getFriendInfo(userIds.map { it.uid })
                        Column( modifier = Modifier.fillMaxWidth().padding(start = 10.dp),) {
                            userInfoList.chunked(3).take(2).forEach { chunk ->
                                Row (horizontalArrangement = Arrangement.spacedBy(25.dp)){
                                    chunk.forEach{userInfo ->
                                        FriendLine(navController, userInfo)
                                    }
                                }
                            }
                        }
                    }
                    item{
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Button(
                                onClick = {navControllerTab.navigate(Routes.ALL_FRIEND)},
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
                            val postsSorted = posts.entries
                                .map { it.key to (it.value as Map<String, Any>) }
                                .sortedByDescending { (it.second["timestamp"] as Long) }
                                .toMap()

                            for ((index, entry) in postsSorted.entries.withIndex()) {
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
                                    SelfPost(post, imageAvatar, "$firstname $lastname", convertToTime(timestamp),
                                        commentViewModel, postViewModel,notificationViewModel,context, comments, true)
                                } else {
                                    SelfPost(post, imageAvatar, "$firstname $lastname", convertToTime(timestamp),
                                        commentViewModel, postViewModel,notificationViewModel,context, comments, false)
                                }

                            }
                        }
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(R.color.pink)
            )
            // Box ở dưới cùng
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                    .height(52.dp) // Thiết lập chiều cao cho Box mới
                    .background(color = MaterialTheme.colorScheme.background), // Bạn có thể đổi màu theo ý thích
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)){
                    Button(
                        onClick = {
                            isPressed = !isPressed
                            showBottomSheet.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                    )
                    {
                        Row(){
                            Text(text= "$firstname $lastname",color= colorResource(R.color.pink), fontSize = 15.sp)
                            Spacer(Modifier.width(10.dp))
                            if (isPressed) {
                                Image(
                                    painter = painterResource(id = R.drawable.uparrow), // Thay đổi thành icon khi bấm
                                    contentDescription = "Icon Pressed",
                                    modifier = Modifier.size(15.dp) // Kích thước của icon
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.down), // Icon mặc định
                                    contentDescription = "Default Icon",
                                    modifier = Modifier.size(15.dp) // Kích thước của icon
                                )
                            }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            navControllerTab.navigate(Routes.SETTING)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.menubar), // Thay đổi thành icon khi bấm
                            contentDescription = "Icon Pressed",
                            modifier = Modifier.size(20.dp) // Kích thước của icon
                        )
                    }
                }
            }
        }
        if (showBottomSheet.value) {
            SignOutPart(navController, profileViewModel, authViewModel, onDismiss = {showBottomSheet.value=false})
        }
    }

}

@Composable

fun Firstline5(
    navController: NavController, imageAvatar: String?, imageBackground: String?,
    firstname: String, lastname: String, posts: Map<String, Any>?
){
    Column(modifier= Modifier.fillMaxWidth()){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(195.dp)) {
            GetNenHinhDaiDien(imageBackground)
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp)
                .align(Alignment.BottomStart)
                .offset(y = 1.dp)) {
                Row( verticalAlignment = Alignment.CenterVertically) {
                    GetHinhDaiDienProfile(imageAvatar)
                    Spacer(Modifier.width(15.dp))
                    Box(modifier=Modifier.offset(y=25.dp)){
                        Row() {
                            Column {
                                if (posts != null) {
                                    Text(text = posts.size.toString(),modifier=Modifier.padding(start = 25.dp))
                                }
                                Text(text = "Bài viết")
                            }
                            Spacer(Modifier.width(20.dp))
                            Column {
                                Text(text = "0",modifier=Modifier.padding(start = 25.dp))
                                Text(text = "Bạn bè")
                            }
                            Spacer(Modifier.width(20.dp))
                            Column {
                                Text(text = "0",modifier=Modifier.padding(start = 25.dp))
                                Text(text = "Theo dõi")
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Spacer(Modifier.height(10.dp))
            Text(text= "$firstname $lastname", modifier=Modifier.padding(start = 15 .dp),
                fontWeight = FontWeight.ExtraBold, fontSize = 20.sp
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {navController.navigate(Routes.PROFILE_EDIT)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    // Đặt kích thước cho nút
                    .border(
                        BorderStroke(1.dp, color = colorResource(R.color.pink)),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .size(width = 200.dp, height = 32.dp)
            ) {
                // Sử dụng Box để căn giữa nội dung
                Box(
                    // Chiếm toàn bộ không gian của nút
                    contentAlignment = Alignment.Center // Căn giữa nội dung
                ) {
                    Text(
                        text = "Chỉnh sửa trang cá nhân",
                        color = colorResource(R.color.pink),
                        fontSize = 12.sp,
                        modifier= Modifier
                            .padding(start = 11.dp)
                            .offset(y = (-1).dp)
                    )
                }
            }
        }
    }
}
@Composable
fun FriendLine(navController: NavController, friend: Map<String,Any>){
    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
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
@Composable
fun GetNenHinhDaiDien(image: String?) {
    // Ảnh chính
    Image(
        painter = rememberAsyncImagePainter(image),
        contentDescription = "Main Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(5.dp, colorResource(R.color.pinkBlur)) // Viền ảnh chính
    )
}
@Composable
fun  GetHinhDaiDienProfile(image: String?){
    Image(
        painter = rememberAsyncImagePainter(image),
        contentDescription = "Circular Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(90.dp) // Kích thước ảnh tròn
            .clip(CircleShape) // Cắt ảnh thành hình tròn
            .border(1.dp, colorResource(R.color.pinkBlur), CircleShape)
    )
}
@Composable
fun  GetHinhDaiDienPost1(image: String?){
    Image(
        painter = rememberAsyncImagePainter(image),
        contentDescription = "Circular Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(41.dp) // Kích thước ảnh tròn
            .clip(CircleShape) // Cắt ảnh thành hình tròn
            .border(1.dp, colorResource(R.color.pinkBlur), CircleShape)
    )
}

@Composable
fun GetHinhDaiDienProfileFriend(img2: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp)) // Thêm spacer ở đây để đẩy hình ảnh xuống
        Image(
            painter = painterResource(img2),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 92.dp, height = 51.dp)
                .clip(RoundedCornerShape(2.dp))
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelfPost(
    post: Post,
    imageAvatar: String?,
    name: String,
    time: String,
    commentViewModel: CommentViewModel,
    postViewModel: PostViewModel,
    notificationViewModel: NotificationViewModel,
    context: Context,
    comments: Map<String, Any>?,
    like: Boolean
){
    val showBottomSheet = remember { mutableStateOf(false) }
    var isToggled by remember { mutableStateOf(like) }
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)

    Column(modifier=Modifier.fillMaxSize()){
        Row(modifier= Modifier
            .fillMaxWidth().padding(start = 10.dp)){
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
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = setText2(name,15),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    )
                }
                Row() {
                    Text(text = time)
                    Spacer(Modifier.width(50.dp))
                    if(post.report=="true") {
                        Image(
                            painter = painterResource(R.drawable.reportpost),
                            contentDescription = "option",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
        Row (modifier = Modifier.fillMaxWidth()){
            Text(text = post.content, modifier = Modifier.padding(start = 10.dp),color = MaterialTheme.colorScheme.onBackground)
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(11.dp))
                if(isToggled){
                    Text(text = "Thích",color=Color.Blue, fontWeight = FontWeight.Bold)
                }
                else{
                    Text(text = "Thích",color=MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.weight(1f))

            Button(onClick={showBottomSheet.value = true},modifier = Modifier.padding(end = 5.dp), colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background)) {
                Image(
                    painter = painterResource(R.drawable.speechbubble),
                    contentDescription = "option",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(11.dp))
                Text(text = "Bình luận",color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold,)
            }
        }
    }
    if (showBottomSheet.value) {
        if (imageAvatar != null) {
            cmtPart(onDismiss = { showBottomSheet.value = false }, name, post.id,post.userID,imageAvatar,
                commentViewModel, notificationViewModel)
        } // Gọi hàm `cmtPart` và ẩn khi hoàn tất
    }
}
