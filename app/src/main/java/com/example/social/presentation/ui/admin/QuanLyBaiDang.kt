package com.example.social.presentation.ui.admin

import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Friend
import com.example.social.data.model.Post
import com.example.social.domain.utils.SetText
import com.example.social.domain.utils.convertToTime
import com.example.social.presentation.ui.GetHinhDaiDienNof
import com.example.social.presentation.ui.SelfPost
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuanLyBaiDang(navController: NavController,
                  allUserViewModel: AllUserViewModel,
                  postViewModel: PostViewModel)
{
    var expanded by remember { mutableStateOf(false) }
    postViewModel.getAllPosts()
    val allPosts = postViewModel.allPosts.collectAsState().value


    // State để lưu số lượng người dùng
    val postIds = mutableListOf<String>()
    allPosts?.forEach{
        post -> for ((index, entry) in post.entries.withIndex()) {
        val friendData = entry.value as? Map<*, *>
        val postID = friendData?.get("id") as? String
        if (postID != null) {
            postIds.add(postID)
            }
        }
    }

    Column(modifier = Modifier.padding(top = 100.dp,start = 20.dp, end = 20.dp))
    {
        Box(modifier = Modifier.border(BorderStroke(1.dp, colorResource(id = R.color.pink)),
            RoundedCornerShape(15.dp)).
        padding(5.dp))
        {
            Row()
            {
                Text(text = "Tổng số bài đăng", fontSize = 23.sp, color = Color.Black)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = postIds.size.toString(), fontSize = 23.sp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))
        Row()
        {
            Text(text = "Danh sách bài đăng", fontSize = 23.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(15.dp))
        LazyColumn() {
            item()
            {
                Spacer(Modifier.height(10.dp))
                if (allPosts != null)
                {
                    for (posts in allPosts.reversed())
                    {
                        for ((index, entry) in posts.entries.withIndex())
                        {
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
                            LaunchedEffect(Unit)
                            {
                                val firstnameResult =
                                    postViewModel.getFirstname(userIDPost.toString())
                                if (firstnameResult != null)
                                    first = firstnameResult
                                val lastnameResult =
                                    postViewModel.getLastname(userIDPost.toString())
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
                            Spacer(Modifier.height(20.dp))
                            ItemQuanLyPost(post, avatar, "$first $last",
                                convertToTime(timestamp),postViewModel)
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .border(BorderStroke(1.dp, colorResource(R.color.pink)), RoundedCornerShape(15.dp))
                        .padding(2.dp)
                ) {
                    Text(
                        text = if (expanded) "Ẩn bớt" else "Xem tất cả",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clickable {
                                expanded = !expanded
                            }
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }
}
@Composable
fun ItemQuanLyPost(post: Post,
                   imageAvatar: String?,
                   name: String,
                   time: String,postViewModel: PostViewModel)
{
    var report by rememberSaveable { mutableStateOf("") }
    var newReport by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val currentReport = postViewModel.getReport(post.userID,post.id).toString()
        report = currentReport
        newReport = if (currentReport == "true") "false" else "true"
    }

    val showList = remember { mutableStateOf<Boolean>(false) }//trang thái button 3 chấm
    Box(modifier = Modifier.border(width = 1.dp,color = colorResource(R.color.pink)).padding(5.dp).fillMaxSize())
    {
        Column()
        {
            Row()
            {
                Image(
                    painter = rememberAsyncImagePainter(imageAvatar),
                    contentDescription = "Circular Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(41.dp) // Kích thước ảnh tròn
                        .clip(CircleShape) // Cắt ảnh thành hình tròn
                        .border(1.dp, colorResource(R.color.pinkBlur), CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column()
                {
                    Row(modifier=Modifier.fillMaxWidth()) {
                        Text(text = name, fontSize = 23.sp)
                        Spacer(Modifier.weight(1f))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    // Gọi hàm updateReport bên trong coroutine
                                    postViewModel.updateReport(post.userID,post.id,newReport)
                                }
                                report = newReport
                                newReport = if (newReport == "true") "false" else "true"
                            },
                            modifier = Modifier.width(75.dp).height(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                        ) {
                            Image(
                                painter = painterResource(R.drawable.meatballsmenuc),
                                contentDescription = "Back",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(25.dp).offset(x = 5.dp)
                            )
                        }
                    }
                    Text(text = time, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
//                PostOptionMenu(showList)
            }
            var isExpanded by remember { mutableStateOf(false) }
            // Kiểm tra chiều dài của nội dung để quyết định có hiển thị nút "Xem thêm" hay không
            val showMoreButton = post.content.length > 15 // Ví dụ, nếu chuỗi dài hơn 15 ký tự
            Row(modifier = Modifier.fillMaxWidth().
            clickable
            {
                if (showMoreButton) isExpanded = !isExpanded// Nhấn để chuyển đổi nếu có nút "Xem thêm")
            })
            {
                SetText(post.content, isExpanded, 30)

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
        }
    }
}
@Composable
fun PostOptionsMenu(uid: String, authViewModel: AuthViewModel,
                    profileViewModel: ProfileViewModel
) {

    var expanded by remember { mutableStateOf(false) }
    var showEmailDialog by remember { mutableStateOf(false) }
    var newEmail by remember { mutableStateOf("") }
    var report by rememberSaveable { mutableStateOf("") }
    var newReport by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    // Gọi hàm lấy vai trò trong LaunchedEffect khi khởi chạy
    LaunchedEffect(Unit) {
        val currentReport = profileViewModel.getMode(uid).toString()
        report = currentReport
        newReport = if (currentReport == "true") "false" else "true"
    }



    Box {
        androidx.compose.material.IconButton(onClick = { expanded = true }) {
            androidx.compose.material.Icon(Icons.Default.MoreVert, contentDescription = "Options")
        }

        androidx.compose.material.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            androidx.compose.material.DropdownMenuItem(onClick = {
                expanded = false
                showEmailDialog = true
            }) {
                androidx.compose.material.Text("Sửa email")
            }
            androidx.compose.material.DropdownMenuItem(onClick = {
                expanded = false
                authViewModel.setDeleted(uid)
                Toast.makeText(
                    context,
                    "Đã xoá thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                androidx.compose.material.Text("Xóa tài khoản")
            }
            androidx.compose.material.DropdownMenuItem(onClick = {
                expanded = false
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

    // Dialog chỉnh sửa email
    if (showEmailDialog) {
        EditEmailDialog(
            newEmail = newEmail,
            onEmailChange = { newEmail = it },
            onConfirm = {
                profileViewModel.updateEmail(newEmail, uid)
                Toast.makeText(
                    context,
                    "Đã sửa thành công",
                    Toast.LENGTH_SHORT
                ).show()
                showEmailDialog = false
            },
            onDismiss = { showEmailDialog = false }
        )
    }
}




