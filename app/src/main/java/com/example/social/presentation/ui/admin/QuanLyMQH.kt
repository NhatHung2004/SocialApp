package com.example.social.presentation.ui.admin

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.social.R
import com.example.social.data.model.User
import com.example.social.domain.utils.convertToTime
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuanLyMQH(navController: NavController,
              allUserViewModel: AllUserViewModel,
              friendViewModel: FriendViewModel)
{
    var expanded by remember { mutableStateOf(false) } // Trạng thái cho danh sách mở rộng
    var visibleUsers by remember { mutableStateOf(false) } // Trạng thái cho danh sách mở rộng
    val users = allUserViewModel.allUsers.collectAsState().value
//    LazyColumn(modifier = Modifier.padding(top = 100.dp, start = 20.dp, end = 20.dp, bottom = 40.dp))
//    {
//        item()
//        {
//            for (user in  users )
//            {
//                val uid = user["uid"].toString()
//                var friends by remember { mutableStateOf<Map<String, Any>?>(null) }
//                LaunchedEffect(Unit) {
//                    val friend = friendViewModel.getFriend(uid)
//                    friends = friend
//                }
//                val name = "${user["firstname"]} ${user["lastname"]}"
//                val avatarUri = Uri.parse(user["avatar"].toString())
//                val countFriend = friends?.size.toString()
//
//                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
//                Box(modifier = Modifier.border(width = 1.dp,
//                    color = colorResource(R.color.pink)).padding(5.dp).fillMaxSize()
//                    .clickable {
//                        visibleUsers = !visibleUsers
//                    })
//                {
//                    Column()
//                    {
//                        Row()
//                        {
//                            GetHinhDaiDien(avatarUri)
//                            Spacer(modifier = Modifier.width(16.dp))
//                            Column()
//                            {
//                                Text(text = name,fontSize = 23.sp)
//                                Text(text = "Có $countFriend bạn bè ")
//                            }
//                        }
//                    }
//                }
//                friends?.let { if(visibleUsers) ListFriends(it,friendViewModel) }
//            }
//        }
//        item()//thanh xem tất cả hoặc ẩn bớt
//        {
//            Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
//            Box(modifier = Modifier.clip(RoundedCornerShape(30.dp))
//                .border(BorderStroke(1.dp, colorResource(R.color.pink)), RoundedCornerShape(15.dp))
//                .padding(2.dp)
//            )
//            {
//                Text(
//                    text = if (expanded) "Ẩn bớt" else "Xem tất cả",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(2.dp)
//                        .clickable
//                        {
//                            expanded = !expanded // Khi nút được nhấn, mở rộng danh sách
//                        }
//                        .wrapContentSize(Alignment.Center) // Căn giữa văn bản
//                )
//            }
//        }
//    }
    val expandedState = remember { mutableStateMapOf<String, Boolean>() }
    LazyColumn(
        modifier = Modifier.padding(top = 100.dp, start = 20.dp, end = 20.dp, bottom = 40.dp)
    ) {
        // Hiển thị từng user
        items(users) { user ->
            val uid = user["uid"].toString()
            var friends by remember { mutableStateOf<Map<String, Any>?>(null) }
            val name = "${user["firstname"]} ${user["lastname"]}"
            val avatarUri = Uri.parse(user["avatar"].toString())
            val isExpanded = expandedState[uid] ?: false // Lấy trạng thái mở rộng của user này
            val countFriend = if(friends != null) friends!!.size else 0

            LaunchedEffect(isExpanded) {
                if (isExpanded && friends == null) {
                    friends = friendViewModel.getFriend(uid)
                }
            }

            Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
                    .clickable {
                        // Chỉ thay đổi trạng thái của user hiện tại
                        expandedState[uid] = !(expandedState[uid] ?: false)
                    }
            ) {
                Column {
                    Row {
                        GetHinhDaiDien(avatarUri)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = name, fontSize = 23.sp)
                            Spacer(modifier = Modifier.height(20.dp))
                            if(friends != null){
                                Text(text = "Có $countFriend bạn bè ")
                            }
                        }
                    }

                    // Hiển thị danh sách bạn bè nếu trạng thái mở rộng của user này là true
                    if (isExpanded) {
                        friends?.let { ListFriends(it, friendViewModel) }
                    }
                }
            }
        }

        // Nút "Xem tất cả" hoặc "Ẩn bớt" (chỉ hiển thị nếu danh sách user > 3)
        if (users.size > 3) {
            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .border(BorderStroke(1.dp, colorResource(R.color.pink)), RoundedCornerShape(15.dp))
                        .padding(2.dp)
                ) {
                    Text(
                        text = if (expandedState.values.any { it }) "Ẩn bớt" else "Xem tất cả",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clickable {
                                // Mở rộng hoặc ẩn tất cả các user
                                val expandAll = expandedState.values.any { it }
                                expandedState.keys.forEach { uid ->
                                    expandedState[uid] = !expandAll
                                }
                            }
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListFriends(friends: Map<String,Any>,friendViewModel: FriendViewModel)
{
    Spacer(modifier = Modifier.fillMaxWidth().height(15.dp))
    Column (modifier = Modifier.padding(start = 40.dp))
    {
        for ((index, entry) in friends.entries.withIndex()) {
            val friendData = entry.value as? Map<*, *>
            val uid = friendData?.get("uid").toString()
            val timestamp = friendData?.get("timestamp") as Long
            var first by remember { mutableStateOf("") }
            var last by remember { mutableStateOf("") }
            var avatar by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                val firstnameResult = friendViewModel.getFirstname(uid)
                if (firstnameResult != null)
                    first = firstnameResult
                val lastnameResult = friendViewModel.getLastname(uid)
                if (lastnameResult != null)
                    last = lastnameResult
                val avatarResult = friendViewModel.getAvatar(uid)
                if (avatarResult != null) {
                    avatar = avatarResult
                }
            }
            ItemFriend(first, last, avatar, timestamp)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemFriend(firstName: String, lastName: String, avatar: String, time: Long)
{
    val avatarUri = Uri.parse(avatar)
    Box(modifier = Modifier.fillMaxSize()) {
        Row ()
        {
            GetHinhDaiDien(avatarUri)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = firstName + lastName)
                Spacer(Modifier.height(5.dp))
                Row(modifier =Modifier.fillMaxWidth()){
                    Text(text ="Vào lúc:" +" ")
                    Text(text= convertToTime(time))
                }
            }
        }
    }
}