package com.example.social.presentation.ui.admin

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Friend
import com.example.social.data.model.Post
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.launch
import com.example.social.data.model.User
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun QuanLyUser(navController: NavController,
               authViewModel: AuthViewModel,
               allUserViewModel: AllUserViewModel,
               friendViewModel: FriendViewModel,
               postViewModel: PostViewModel
) {

    // State để lưu số lượng người dùng
    var totalUsers by remember { mutableIntStateOf(0) }
    val avatar = R.drawable.khac
    val backgroundAvatar = R.drawable.background

    val coroutineScope = rememberCoroutineScope()
    val showAddUserDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) } // Trạng thái cho danh sách mở rộng
    allUserViewModel.getAllUsersInfo()
    val users by allUserViewModel.allUsers.collectAsState() // Danh sách tất cả user trên firebase



    // Gọi hàm countUsers trong một coroutine
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            totalUsers = CountObject("users")
        }
    }
    Column(modifier = Modifier.padding(top = 100.dp, start = 20.dp, end = 20.dp, bottom = 40.dp))
    {
        Box(
            modifier = Modifier.border(
                BorderStroke(1.dp, colorResource(id = R.color.pink)),
                RoundedCornerShape(15.dp)
            ).padding(5.dp)
        )
        {
            Row()
            {
                Text(text = "Tổng số user", fontSize = 23.sp, color = Color.Black)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$totalUsers", fontSize = 23.sp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))
        Row()
        {
            Text(text = "Danh sách user", fontSize = 23.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    showAddUserDialog.value = true
                },
                modifier = Modifier.height(23.dp),
                contentPadding = PaddingValues(2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            )
            {
                Spacer(modifier = Modifier.weight(1f))
                Image(painterResource(R.drawable.plus), contentDescription = null)
            }
            if (showAddUserDialog.value) {
                AddUserDialog(
                    context,
                    onDismiss = { showAddUserDialog.value = false },
                    onAdd = { ho, ten, email, password ->
                        authViewModel.register(
                            email, password, ho, ten, "Khác", "",
                            "android.resource://com.example.social/drawable/$avatar",
                            "android.resource://com.example.social/drawable/$backgroundAvatar",
                            "offline", "false", "false"
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(15.dp))

        LazyColumn {
            items(users.take(if (expanded) users.size else 3),
                key = { user -> user["uid"].toString() }) { user ->

                // Gọi ItemQuanLyUser
                ItemQuanLyUser(user,postViewModel)
                Spacer(modifier = Modifier.height(15.dp))
            }

            item()//thanh xem tất cả hoặc ẩn bớt
            {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(30.dp)).border(
                        BorderStroke(1.dp, colorResource(R.color.pink)),
                        RoundedCornerShape(15.dp)
                    ).padding(2.dp)
                )
                {
                    Text(
                        text = if (expanded) "Ẩn bớt" else "Xem tất cả",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clickable
                            {
                                expanded = !expanded // Khi nút được nhấn, mở rộng danh sách
                            }
                            .wrapContentSize(Alignment.Center) // Căn giữa văn bản
                    )
                }
            }
        }
    }
}
@Composable
fun ItemQuanLyUser(
    user: Map<String, Any>,
    postViewModel: PostViewModel,
    friendViewModel: FriendViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
) {
    val uid = user["uid"].toString()
    val name = "${user["firstname"]} ${user["lastname"]}"
    val status = user["status"].toString()
    val avatarUri = Uri.parse(user["avatar"].toString())
    val mode = user["mode"].toString()

    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value
// Điều kiện thể hiện trạng thái
    val imageResource = if (status == "online") {
        R.drawable.online // Thay bằng ảnh khi điều kiện đúng
    } else {
        R.drawable.offline // Thay bằng ảnh khi điều kiện sai
    }
    // Trạng thái cục bộ để lưu số lượng bài viết và bạn bè
    var postCount by remember { mutableStateOf(0) }
    var friendCount by remember { mutableStateOf(0) }

    // Lấy số lượng bài viết và bạn bè khi uid thay đổi
    LaunchedEffect(uid) {
        postCount = postViewModel.countPost(uid)
        friendCount = friendViewModel.countFriend(uid)
    }

    Box(modifier = Modifier
        .clip(RoundedCornerShape(32.dp)) // Clip trước
        .background(colorResource(R.color.lightGrey)) // Nền sau
        .height(180.dp),
        contentAlignment = Alignment.Center
    )
    {
        Column(modifier = Modifier.padding(start = 15.dp))
        {
            Row()
            {
                GetHinhDaiDien(avatarUri)
                Spacer(modifier = Modifier.width(25.dp))
                Column()
                {
                    Text(text = name,fontSize = 23.sp)
                    Row()
                    {
                        Column(verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Text(text = postCount.toString(),fontSize = 15.sp)
                            Text(text = "Bài viết",fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Text(text = friendCount.toString(),fontSize = 15.sp)
                            Text(text = "Bạn bè",fontSize = 15.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = if(mode =="true") "Người kiểm duyệt" else "")
                Spacer(modifier = Modifier.weight(1f))
                UserOptionsMenu(uid,authViewModel,profileViewModel)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(top = 20.dp))
            {
                Box(modifier = Modifier.padding(5.dp).
                clip(RoundedCornerShape(50.dp)))
                {
                    Row()
                    {
                        Image(
                            painter = painterResource(id = imageResource), contentDescription = null,
                            modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = status)
                    }
                }
                Spacer(modifier = Modifier.width(120.dp))
                Image(
                    painterResource(R.drawable.clock), contentDescription = null,
                    modifier = Modifier.size(40.dp).padding(top = 10.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column()
                {
                    Text(text = "Ngày lập:", fontSize = 15.sp)
                    Text(text = "11/11/2021", fontSize = 15.sp)//chưa lấy được real time
                }
            }
        }
    }
}

@Composable
fun AddUserDialog(
    context : Context,
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String) -> Unit
) {
    var ho by remember { mutableStateOf("") }
    var ten by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Thêm người dùng")

                OutlinedTextField(
                    value = ho,
                    onValueChange = { ho = it },
                    label = { Text("Họ") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ten,
                    onValueChange = { ten = it },
                    label = { Text("Tên") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    TextButton(onClick = {
                        if (password.isNotEmpty() && email.isNotEmpty()
                            && ho.isNotEmpty() && ten.isNotEmpty()) {
                            onAdd(ho, ten ,email, password)
                            onDismiss()
                            Toast.makeText(
                                context,
                                "Đã thêm thành công $ho $ten",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text("Thêm")
                    }
                }
            }
        }
    }
}
@Composable
fun UserOptionsMenu(uid: String, authViewModel: AuthViewModel,
                    profileViewModel: ProfileViewModel,) {

    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var mode by rememberSaveable { mutableStateOf("") }
    var newMode by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }

    first = profileViewModel.firstname.collectAsState().value
    last = profileViewModel.lastname.collectAsState().value

    // Gọi hàm lấy vai trò trong LaunchedEffect khi khởi chạy
    LaunchedEffect(Unit) {
        val currentMode = profileViewModel.getMode(uid).toString()
        mode = currentMode
        newMode = if (currentMode == "true") "false" else "true"
    }


    Box {
        androidx.compose.material.IconButton(onClick = { expanded = true }) {
            androidx.compose.material.Icon(Icons.Default.MoreHoriz, contentDescription = "Options")
        }

        androidx.compose.material.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            androidx.compose.material.DropdownMenuItem(onClick = {
                expanded = false
                showDialog = true
            }) {
                androidx.compose.material.Text("Sửa họ tên bất hợp pháp")
            }
//            androidx.compose.material.DropdownMenuItem(onClick = {
//                expanded = false
//                Toast.makeText(
//                    context,
//                    "Đã xoá thành công",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }) {
//                androidx.compose.material.Text("Xóa tài khoản")
//            }
            androidx.compose.material.DropdownMenuItem(onClick = {
                expanded = false
                // Thay đổi vai trò trong ViewModel
                authViewModel.setMode(uid, newMode)

                // Cập nhật trạng thái trực tiếp
                mode = newMode
                newMode = if (newMode == "true") "false" else "true"
                Toast.makeText(
                    context,
                    "Đã chuyển vai trò thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                androidx.compose.material.Text("Chuyển đổi vai trò")
            }
        }
    }

    // Dialog sửa họ tên
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Sửa họ tên") },
            text = {
                Column {
// TextField cho Họ
                    TextField(
                        value = first,
                        onValueChange = { first = it },
                        label = { Text("Họ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa 2 TextField
                    // TextField cho Tên
                    TextField(
                        value = last,
                        onValueChange = { last = it },
                        label = { Text("Tên") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    profileViewModel.updateHoTen(last, first, uid)
                    showDialog = false
                }) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun GetHinhDaiDien(img2 : Uri)
{
    Image(
        painter= rememberAsyncImagePainter(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color = Color.Black,RoundedCornerShape(16.dp))
    )
}
suspend fun CountObject(type: String): Int {
    val db = FirebaseFirestore.getInstance()
    return try {
        val result = db.collection(type).get().await()
        result.size() // Trả về số lượng tài liệu trong collection "users"
    } catch (e: Exception) {
        e.printStackTrace()
        0 // Trả về 0 nếu có lỗi xảy ra
    }
}