package com.example.social.presentation.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Post
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.example.social.presentation.viewmodel.PostFocusViewModel
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
fun PostFocus(postFocusViewModel: PostFocusViewModel, postId:String, userId:String, profileViewModel: ProfileViewModel, commentViewModel: CommentViewModel,notificationViewModel: NotificationViewModel){
    val posts = postFocusViewModel.posts.collectAsState().value
    commentViewModel.getComments(postId)
    val comments = commentViewModel.comments.collectAsState().value
    val context= LocalContext.current
    commentViewModel.getComments(userId)
    var name by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        name= postFocusViewModel.getFirstname(userId).toString()+" "+postFocusViewModel.getLastname(userId).toString()
        avatar = postFocusViewModel.getAvatar(userId).toString()
    }
    postFocusViewModel.getPosts(userId)

    commentViewModel.getComments(postId)


    LazyColumn(modifier = Modifier.fillMaxSize()){
        item{
            Row(){
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
                Spacer(Modifier.width(50.dp))
                Text(text=name, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground
                    , modifier = Modifier.padding(top=5.dp), fontSize = 25.sp)
            }
        }
        item{
            if (posts != null) {
                for ((index, entry) in posts.entries.withIndex()) {
                    val postData = entry.value as? Map<*, *>
                    val imageUris = postData?.get("imageUris") as List<String>
                    val liked = postData["liked"] as List<String>
                    val content = postData["content"]
                    val timestamp = postData["timestamp"] as Long
                    val id = postData["id"]
                    val userID = postData["userID"]
                    val post = Post(id.toString(), userID.toString(), content.toString(),
                        timestamp, imageUris, liked)
                    val currentDate = Instant.ofEpochMilli(post.timestamp)
                        .atZone(ZoneId.systemDefault()) // Lấy múi giờ hệ thống
                        .toLocalDate()
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val formattedDate = currentDate.format(formatter)
                    val like = post.liked.contains(Firebase.auth.currentUser!!.uid)
                    if(id==postId){
                        if(like) {
                            FocusPost(
                                post,
                                avatar,
                                name,
                                formattedDate,
                                commentViewModel,
                                postFocusViewModel,
                                notificationViewModel, context,
                                comments,
                                true
                            )
                        }
                        else{
                            FocusPost(
                                post,
                                avatar,
                                name,
                                formattedDate,
                                commentViewModel,
                                postFocusViewModel,
                                notificationViewModel,
                                context,
                                comments,
                                false
                            )
                        }
                    }
                }
            }
        }
        item{
            Spacer(Modifier.height(5.dp))
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FocusPost(
    post: Post,
    imageAvatar: String?,
    name: String,
    time: String,
    commentViewModel: CommentViewModel,
    postFocusViewModel: PostFocusViewModel,
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
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                )
                Text(text = time)
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
                postFocusViewModel.updateLiked(post.id, post.userID, Firebase.auth.currentUser!!.uid)
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
        cmtPart(onDismiss = { showBottomSheet.value = false }, name, post.id,post.userID,
            commentViewModel, comments,notificationViewModel) // Gọi hàm `cmtPart` và ẩn khi hoàn tất
    }
}