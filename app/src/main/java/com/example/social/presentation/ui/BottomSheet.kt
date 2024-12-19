package com.example.social.presentation.ui


import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.domain.utils.toPrettyTime
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendReqToSendBottomSheet(navController: NavController,onDismiss:()->Unit){
    val openBottomSheet by remember { mutableStateOf(true) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier=Modifier.fillMaxWidth().height(80.dp),
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally

                ){}
            }
        ) {
            Button(onClick = {navController.navigate(Routes.ALL_FRIEND_SEND)},modifier=Modifier.fillMaxSize().padding(start = 0.dp)
                ,colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                )){
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
                    Box(
                        modifier = Modifier
                            .size(32.dp) // Kích thước của hình tròn
                            .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                        , contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.searching),//R.drawable.man
                            contentDescription = "avatar",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text="Lời mời đã gửi" ,fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendBottomSheet(friend:Map<String,Any>,userIds:List<String>, time:Long, isPressed: MutableState<Boolean>, friendViewModel: FriendViewModel, onDismiss:()->Unit){
    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val userId=friend["uid"]

    val openBottomSheet by remember { mutableStateOf(true) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier=Modifier.fillMaxWidth(),
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = { },
                        modifier = Modifier.fillMaxWidth().padding(start = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )){
                        Row(modifier = Modifier.fillMaxWidth().padding(start = 2.dp)){
                            Image(
                                painter= rememberAsyncImagePainter(avatarUri),
                                contentDescription="avatar",
                                contentScale = ContentScale.Crop,
                                modifier= Modifier
                                    .size(45.dp)
                                    .clip(RoundedCornerShape(35.dp))
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(){
                                Text(text=name
                                    , color = MaterialTheme.colorScheme.onBackground
                                    , fontSize = 19.sp
                                    ,modifier=Modifier.padding(top=2.dp))
                                Spacer(Modifier.height(5.dp))
                                Text(text= time.toPrettyTime()
                                    , color = MaterialTheme.colorScheme.onBackground
                                    , fontSize = 12.sp
                                    ,modifier=Modifier.padding(top=2.dp))
                            }
                        }
                    }
                }
            }
        ) {
            Column(modifier=Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                Spacer(Modifier.height(2.dp))
                Divider(
                    color = colorResource(R.color.lightGrey),
                    thickness = 1.dp
                )
                Spacer(Modifier.height(2.dp))
                Button(
                    onClick = {
                        friendViewModel.deleteFriend(Firebase.auth.currentUser!!.uid, userId.toString())
                        friendViewModel.deleteFriend(userId.toString(),Firebase.auth.currentUser!!.uid)
                        isPressed.value=true
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 0.dp).height(120.dp).clip(RectangleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                    ,shape = RoundedCornerShape(4.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp) // Kích thước của hình tròn
                                .background(MaterialTheme.colorScheme.onBackground, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.exit),
                                contentDescription = "avatar",
                                colorFilter = ColorFilter.tint(Color.Red),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(text = "Hủy kết bạn với $name", color = Color.Red, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationBottomSheet(notificationViewModel: NotificationViewModel, userId: String, postID: String, context: Context, contentNof:String, onDismiss:()->Unit){
    val openBottomSheet by remember { mutableStateOf(true) }
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier=Modifier.fillMaxWidth().height(80.dp),
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally

                ){}
            }
        ) {
            Button(onClick = {
                notificationViewModel.deletePostNotification(Firebase.auth.currentUser!!.uid,userId,postID,contentNof)
                Toast.makeText(context,"Đã xóa",Toast.LENGTH_SHORT).show()
                onDismiss()
            },modifier=Modifier.fillMaxSize().padding(start = 0.dp)
                ,colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                )){
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
                    Box(
                        modifier = Modifier
                            .size(32.dp) // Kích thước của hình tròn
                            .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                        , contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.searching),//R.drawable.man
                            contentDescription = "avatar",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text="Xóa Thông báo" ,fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cmtPart(
    onDismiss: () -> Unit,
    posterName: String,
    postID: String,
    userId:String,
    img2: String,
    commentViewModel: CommentViewModel,
    notificationViewModel: NotificationViewModel
){
    val openBottomSheet by remember { mutableStateOf(true) }
    commentViewModel.getComments(postID)

    if(openBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text="Bình luận",style=MaterialTheme.typography.titleLarge)
                    Spacer(modifier=Modifier.height(10.dp))
                }
            }
        ) {
            Divider(
                color = colorResource(R.color.pink ),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            listCmt(posterName, postID,userId, commentViewModel,notificationViewModel,img2) // Gọi hàm hiển thị danh sách bình luận
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun listCmt(
    posterName: String,
    postID: String,
    userId: String,
    commentViewModel: CommentViewModel
    ,notificationViewModel: NotificationViewModel, img2: String
){
    commentViewModel.getComments(postID)
    val commentsState by commentViewModel.comments.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        // Đặt LazyColumn để cuộn qua danh sách bình luận
        LazyColumn(modifier = Modifier
            .weight(1f) // Đảm bảo nó chiếm không gian còn lại
            .background(MaterialTheme.colorScheme.background).fillMaxWidth()) {
            item {
                infoCmt(commentsState, commentViewModel)
            }
        }
        Spacer(Modifier.height(4.dp))
        // TextField nằm bên dưới
        textField(posterName, postID, commentViewModel, userId, notificationViewModel, img2) // Căn giữa dưới
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun infoCmt(comments: Map<String, Any>?, commentViewModel: CommentViewModel) {
    Spacer(Modifier.height(20.dp))
    if (comments != null) {
        val commentsSorted = comments.entries
            .map { it.key to (it.value as Map<String, Any>) }
            .sortedByDescending { (it.second["timestamp"] as Long) }
            .toMap()

        for ((index, entry) in commentsSorted.entries.withIndex()) {
            val cmtData = entry.value as? Map<*, *>
            val contentCmt = cmtData?.get("content") as? String
            val timestamp = cmtData?.get("timestamp") as Long
            val time = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val uid = cmtData["uid"] as? String
            var name by remember { mutableStateOf("") }
            var avatar by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                if (uid != null) {
                    val nameResutl = commentViewModel.getName(uid)
                    name = nameResutl
                    val avatarResult = commentViewModel.getAvatar(uid).toString()
                    avatar = avatarResult
                }
            }

            Row(){
                getHinhDaiDienCmt(avatar)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold
                    )
                    if (contentCmt != null) {
                        Text(text=contentCmt)
                    }
                }
                Spacer(Modifier.width(5.dp))
                Text(text = time)
            }
            Spacer(Modifier.height(41.dp))
        }
    }

}
@Composable
fun getHinhDaiDienCmt(img2 : String){
    Image(
        painter= rememberAsyncImagePainter(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textField(posterName: String, postID: String, commentViewModel: CommentViewModel, userId: String,notificationViewModel: NotificationViewModel, img2: String){
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)
    commentViewModel.getComments(postID)

    var avatar by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val avatarResult = commentViewModel.getAvatar(Firebase.auth.currentUser!!.uid).toString()
        avatar = avatarResult
    }
    Row() {
        getHinhDaiDienCmt5(avatar)
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            placeholder = { Text("Bình luận về bài viết của $posterName") }, // Sử dụng placeholder
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    commentViewModel.updateComment("cmt", text, postID)
                    if(userId!=Firebase.auth.currentUser!!.uid) {
                        notificationViewModel.updateNotificationToFireStore(
                            Firebase.auth.currentUser!!.uid,
                            postID,
                            notificationContents[3],
                            "notRead",
                            userId
                        )
                    }
                    Toast.makeText(context,"Đã gửi",Toast.LENGTH_SHORT).show()
                    text=""
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )
    }
}
@Composable
fun getHinhDaiDienCmt5(img2 :String){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp)) // Thêm spacer ở đây để đẩy hình ảnh xuống
        Image(
            painter = rememberAsyncImagePainter(img2),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(41.dp)
                .clip(RoundedCornerShape(35.dp))
        )
    }
}