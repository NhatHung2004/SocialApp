package com.example.social.presentation.ui


import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.db.icons
import com.example.social.db.userPostDataProvider
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendReqToSendBottomSheet(navController: NavController,onDismiss:()->Unit){
    var openBottomSheet by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier=Modifier.fillMaxWidth().height(80.dp),
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally

                ){}
            }
        ) {
            Button(onClick = {navController.navigate(Routes.ALL_FRIEND_SEND)},modifier=Modifier.fillMaxSize().padding(start = 0.dp)
                ,colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )){
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
                    Box(
                        modifier = Modifier
                            .size(32.dp) // Kích thước của hình tròn
                            .background(Color.Gray, shape = CircleShape)
                        , contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.searching),//R.drawable.man
                            contentDescription = "avatar",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text="Lời mời đã gửi",color=Color.Black,fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendBottomSheet(friend:Map<String,Any>,userIds:List<String>, isPressed: MutableState<Boolean>, friendViewModel: FriendViewModel, onDismiss:()->Unit){
    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val userId=friend["uid"]

    var openBottomSheet by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier=Modifier.fillMaxWidth().height(150.dp),
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = { },
                        modifier = Modifier.fillMaxWidth().padding(start = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
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
                                    , color = Color.Black
                                    , fontSize = 19.sp
                                    ,modifier=Modifier.padding(top=2.dp))
                            }
                        }
                    }
                }
            }
        ) {
            Column(modifier=Modifier.fillMaxWidth().background(Color.White)) {
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
                        friendViewModel.getFriends(Firebase.auth.currentUser!!.uid)
                        friendViewModel.getFriends(userId.toString())
                        friendViewModel.getFriendInfo(userIds)
                        isPressed.value=true
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 0.dp).height(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp) // Kích thước của hình tròn
                                .background(Color.Gray, shape = CircleShape),
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
fun cmtPart(
    onDismiss: () -> Unit,
    posterName: String,
    postID: String,
    commentViewModel: CommentViewModel,
    comments: Map<String, Any>?
){
    val openBottomSheet by remember { mutableStateOf(true) }
    commentViewModel.getComments(postID)

    if(openBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(Color.White),
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
            listCmt(posterName, postID, commentViewModel, comments) // Gọi hàm hiển thị danh sách bình luận
        }
    }
}
@Composable
fun listCmt(
    posterName: String,
    postID: String,
    commentViewModel: CommentViewModel,
    comments: Map<String, Any>?
){
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        // Đặt LazyColumn để cuộn qua danh sách bình luận
        LazyColumn(modifier = Modifier
            .weight(1f) // Đảm bảo nó chiếm không gian còn lại
            .background(Color.White).fillMaxWidth()) {
            item {
                infoCmt(comments, commentViewModel)
            }
        }
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        icon()
        Spacer(Modifier.height(4.dp))
        // TextField nằm bên dưới
        textField(posterName, postID, commentViewModel) // Căn giữa dưới
    }
}
@Composable
fun infoCmt(comments: Map<String, Any>?, commentViewModel: CommentViewModel) {
    Spacer(Modifier.height(20.dp))
    if (comments != null) {
        for ((index, entry) in comments.entries.withIndex()) {
            val cmtData = entry.value as? Map<*, *>
            val contentCmt = cmtData?.get("content") as? String
            val timestamp = cmtData?.get("timestamp") as Long
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
                Text(text = timestamp.toString())
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
fun textField(posterName: String, postID: String, commentViewModel: CommentViewModel){
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    Row() {
        getHinhDaiDienCmt5(R.drawable.avt2)
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
                    Toast.makeText(context,"Đã gửi",Toast.LENGTH_SHORT).show()
                }
            ),
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
}
@Composable
fun getHinhDaiDienCmt2(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}
@Composable
fun getHinhDaiDienCmt5(img2 : Int){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp)) // Thêm spacer ở đây để đẩy hình ảnh xuống
        Image(
            painter = painterResource(img2),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(41.dp)
                .clip(RoundedCornerShape(35.dp))
        )
    }
}
@Composable
fun icon(){
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top=7.dp)){
        items(10){ index->//neu lay cac phan tu trong data file thi items(icons.size)
            val icon=icons[0]
            Box(
                modifier= Modifier
                    .size(25.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Green) // Nền màu trắng
            ){
                getHinhDaiDienCmt2(icon.iconRes)

            }
        }
    }
}