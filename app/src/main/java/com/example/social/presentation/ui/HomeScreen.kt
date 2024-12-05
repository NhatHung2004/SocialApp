package com.example.social.presentation.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.social.db.cmtPart
import com.example.social.db.userAvatars
import com.example.social.db.userPostDataProvider
import com.example.social.db.userPosts
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(navController: NavController){//Đây là hàm chính để tạo ra giao diện của màn hình chính trong ứng dụng
    val userPost = remember { userPostDataProvider.userPostList }//lấy giá trị từ userPostList của đối tượng userPostDataProvider
    //và ghi nhớ vào remember(giá trị userPost sẽ được cập nhật lại khi hàm homeScreen được gọi )
    val likedStates = remember { mutableStateMapOf<Int, Boolean>() }// Lưu trạng thái liked của từng bài viết
    val listState = rememberLazyListState()
    LazyColumn (
        modifier=Modifier.fillMaxWidth(),
        state = listState
    ){
        item{
            SubList1()
            Spacer(modifier = Modifier.height(16.dp))
        }
        subListContent(userPost, likedStates)//truyền dữ liệu vừa lấy từ userPostDataProvider vào hàm subListContent
    }
}
@Composable
fun SubList1(profileViewModel: ProfileViewModel = viewModel(), friendViewModel: FriendViewModel = viewModel()){//Tạo một danh sách cuộn ngang cho các avatar của người dùng.
    val friends=friendViewModel.friends.collectAsState().value
    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value

    val firstname = profileViewModel.firstname.collectAsState().value
    val lastname = profileViewModel.lastname.collectAsState().value
    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid,"friends")

    val userIds = remember(friends) {
        friends?.mapNotNull { (key, value) ->
            (value as? Map<*, *>)?.get("uid")?.toString()
        }.orEmpty()
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top=16.dp)){
        item(){
            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier=Modifier.padding(start = 5.dp)) {
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
                    ){
                        Icon( imageVector = Icons.Default.Add,
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
                Text(text=lastname)
            }
        }
        item{
            val userInfoList = friendViewModel.userInfo.collectAsState().value
            if (userInfoList.isEmpty() && userIds.isNotEmpty()) {
                friendViewModel.getFriendInfo(userIds)
            }
            userInfoList.forEachIndexed{index,userInfo->
                val name=" ${userInfo["lastname"]}"
                val avatarUri= Uri.parse(userInfo["avatar"].toString())
                val status=userInfo["status"].toString()
                val userId = userIds.getOrNull(index)
                if (userId != null) {
                    Column (horizontalAlignment = Alignment.CenterHorizontally,){
                        Box() {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        if (status == "online") Color.Green else Color.Red // Thay đổi màu dựa trên trạng thái
                                    )
                                    .align(Alignment.BottomEnd)
                                    .zIndex(1f)
                            ){}
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
                }
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}


fun LazyListScope.subListContent(userPosts: List<userPosts>, likedStates: MutableMap<Int, Boolean>){//Hiển thị danh sách các bài viết của người dùng.
    items(userPosts.size) { index ->
        val post = userPosts[index]
        val isLiked = likedStates[index] ?: post.isLiked //Kiểm tra xem trạng thái liked đã được lưu trong likedStates chưa. Nếu chưa, nó sẽ sử dụng giá trị mặc định từ post.isLiked.

        UserListItems(userPosts = post.copy(isLiked=isLiked)) { liked ->
            likedStates[index] = liked// Cập nhật trạng thái liked khi nhấn nút like
            post.count += if (liked) 1 else -1
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun UserListItems(userPosts: userPosts,//Hiển thị nội dung của một bài viết.
                  onLikeChanged: (Boolean) -> Unit){// Thêm hàm callback để cập nhật trạng thái liked
    //->: Dấu này được sử dụng để chỉ ra rằng đây là một hàm
    //Unit trong Kotlin tương tự như void
    //Khi người dùng nhấn nút Like, hàm onLikeChanged sẽ được gọi với giá trị true hoặc false (tùy thuộc vào trạng thái hiện tại của nút Like).
    val isLiked=userPosts.isLiked
    val likeTextColor = if (isLiked) Color.Blue else Color.Black
    val likeImage=if (isLiked) painterResource(userPosts.blueLikeIcon) else painterResource(userPosts.like)
    val showBottomSheet = remember { mutableStateOf(false) }
    Column (
        modifier=Modifier.fillMaxWidth()
    ){
        Row(modifier = Modifier.fillMaxWidth()){
            GetHinhDaiDienPost(userPosts.avt.avatarRes)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(text=userPosts.name,
                    fontWeight = FontWeight.ExtraBold

                )
                Text(text=userPosts.time)
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter= painterResource(userPosts.optionIcon),
                contentDescription = "option Icon",
                modifier = Modifier.size(24.dp)
            )
        }
        Row (modifier = Modifier.fillMaxWidth()){
            Text(text=userPosts.content,
                modifier=Modifier.padding(start=10.dp))
            Spacer(Modifier.width(10.dp))
            Image(
                painter= painterResource(userPosts.postIcon),
                contentDescription = "post Icon",
                modifier = Modifier.size(24.dp)
            )
        }
        Image(
            painter= painterResource(userPosts.postPic),
            contentDescription = "post Pic",
            modifier = Modifier.height(390.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(11.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Image(
                painter= painterResource(userPosts.blueLikeIcon),
                contentDescription="option",
                modifier = Modifier.size(16.dp)
            )
            Image(
                painter= painterResource(userPosts.loveIcon),
                contentDescription="option",
                modifier = Modifier.size(16.dp)
            )
            Text(text=userPosts.count.toString()+userPosts.quantity)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { onLikeChanged(!isLiked) }, // Đổi trạng thái khi nhấn
                //Gọi hàm callback: Khi người dùng nhấn nút, hàm callback onLikeChanged sẽ được gọi với trạng thái mới (đã bị đảo ngược), giúp cập nhật trạng thái "liked" trong dữ liệu của bạn.
                modifier = Modifier.padding(start = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Image(
                    painter = likeImage,
                    contentDescription = "option",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(11.dp))
                Text(text = userPosts.likeText,color = likeTextColor, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.weight(1f))

            Button(onClick={showBottomSheet.value = true},modifier = Modifier.padding(end = 5.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.White)) {
                Image(
                    painter = painterResource(userPosts.comment),
                    contentDescription = "option",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(11.dp))
                Text(text = userPosts.cmtText,color=Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
    if (showBottomSheet.value) {
        cmtPart(onDismiss = { showBottomSheet.value = false }, posterName =userPosts.name) // Gọi hàm `cmtPart` và ẩn khi hoàn tất
    }
}

@Composable
fun GetHinhDaiDien(img1 : Int){
    Image(
        painter= painterResource(img1),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier.size(60.dp)
    )
}
@Composable
fun GetHinhDaiDienPost(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(41.dp)
            .clip(RoundedCornerShape(25.dp))
    )
}







