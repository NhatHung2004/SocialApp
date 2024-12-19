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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.data.model.Post
import com.example.social.domain.utils.SetText
import com.example.social.domain.utils.convertToTime
import com.example.social.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuanLyBaiDang(navController: NavController,
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

    Column(modifier = Modifier.padding(top = 110.dp))
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
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.padding(start = 15.dp))
        {
            Text(text = "Danh sách bài đăng", fontSize = 25.sp,
                fontWeight = FontWeight.Bold,color = Color.Black)
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            item()
            {
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
                            Spacer(Modifier.height(10.dp))
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
    Box(modifier = Modifier.fillMaxSize()
        .height(520.dp)
        .background(color = colorResource(R.color.lightGrey))
        .border(
            BorderStroke(1.dp, color = Color.Black), // Viền 1dp màu hồng

        ),
        contentAlignment = Alignment.Center)
    {
        Column(modifier = Modifier.padding(15.dp))
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
                        if(post.report == "true"){
                            Spacer(Modifier.width(30.dp))
                            Image(
                                painter = painterResource(R.drawable.reportpost),
                                contentDescription = "option",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        PostOptionsMenu(post,postViewModel)
                    }
                    Text(text = time, fontSize = 15.sp)
                }
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
    Spacer(Modifier.height(20.dp))
}
@Composable
fun PostOptionsMenu(post: Post, postViewModel: PostViewModel
) {

    var expanded by remember { mutableStateOf(false) }
    var report by rememberSaveable { mutableStateOf("") }
    var newReport by rememberSaveable { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
                coroutineScope.launch {
                    // Gọi hàm updateReport bên trong coroutine
                    val currentReport = postViewModel.getReport(post.userID,post.id).toString()
                    report = currentReport
                    newReport = if (currentReport == "true") "false" else "true"
                    text = report
                    postViewModel.updateReport(post.userID,post.id,newReport)
                }
                report = newReport
                newReport = if (newReport == "true") "false" else "true"
                text = newReport
                Toast.makeText(
                    context,
                    if(report == "true") "Đã gỡ cờ thành công" else "Đã gắn cờ thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                androidx.compose.material.Text(text = "Gắn/gỡ cờ")
            }
            androidx.compose.material.DropdownMenuItem(onClick = {
                expanded = false
                postViewModel.deletePost(post.userID,post.id)
                Toast.makeText(
                    context,
                    "Đã xoá thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                androidx.compose.material.Text("Xóa")
            }
        }
    }
}