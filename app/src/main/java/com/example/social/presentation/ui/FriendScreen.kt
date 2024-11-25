package com.example.social.presentation.ui

import com.example.social.R
import com.example.social.db.userPostDataProvider

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun FriendScreen(navController: NavController) {
    LazyColumn( modifier=Modifier.fillMaxSize()) {
        item{
            FirstLine1()
        }
        item{
            Tab1(navController)

        }
        item{
            RecommendFriendTab()
        }
    }
}
@Composable
fun FirstLine(){
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.padding(top = 8.dp)){
        Text(
            text = "Bạn Bè", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.width(50.dp))

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                // Đặt kích thước cho nút
                .border(
                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                    shape = RoundedCornerShape(15.dp)
                )
                .size(width = 200.dp, height = 37.dp)
        ) {
            // Sử dụng Box để căn giữa nội dung
            Box(
                // Chiếm toàn bộ không gian của nút
                contentAlignment = Alignment.Center // Căn giữa nội dung
            ) {
                Row( verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                    modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.searching),
                        contentDescription = "option Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Tìm kiếm",
                        color = colorResource(R.color.pink),
                        fontSize = 20.sp,
                    )
                }
            }
        }

    }
}
@Composable
fun Tab1(navController: NavController) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { RequestTab.entries.size })
    val selectedIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = selectedIndex.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp),
            containerColor = Color.White,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex.value])
                        .height(4.dp)
                        .background(Color(0xFFFF4081))
                )
            }
        ) {
            RequestTab.entries.forEachIndexed { index, currentTab ->
                Tab(
                    selected = selectedIndex.value == index,
                    selectedContentColor = Color(0xFFFF4081),
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    },
                    text = {
                        Text(text = currentTab.text)
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Thay đổi chiều cao cho HorizontalPager
        ) { pageIndex ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(top = 25.dp)
            ) {
                when (RequestTab.entries[pageIndex]) {
                    RequestTab.ReceiveRequest -> { ReqTab(navController) }
                    RequestTab.SentRequest -> { ReqTab(navController) }
                }
            }
        }
    }
}
@Composable
fun RecommendFriendTab(){
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(320.dp)
                .background(Color.Black.copy(alpha = 0.5f)) // Điều chỉnh độ mờ
                .align(Alignment.Center) // Căn giữa
        )
    }
    Spacer(Modifier.height(16.dp))
    Column {
        Text(text="Gợi ý kết bạn",
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.height(16.dp))
        userPostDataProvider.friendList.forEach {friend->
            Row(modifier = Modifier.fillMaxWidth()) {
                GetHinhDaiDienAllFriend(friend.avtFriend.avatarRes)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = friend.nameFriend,
                    )
                    Spacer(Modifier.width(11.dp))
                    Text(text = friend.timeFriend)
                    Row(){
                        val context = LocalContext.current
                        Button(onClick={Toast.makeText(context,"Đã đồng ý kết bạn vơi " + friend.nameFriend,Toast.LENGTH_SHORT).show()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.pink)
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .size(width = 132.dp, height = 37.dp)

                        ){
                            Text(text="Thêm bạn",color=Color.White)
                        }
                        Spacer(Modifier.weight(0.7f))
                        Button(onClick={Toast.makeText(context,"Đã từ chối kết bạn vơi " + friend.nameFriend,Toast.LENGTH_SHORT).show()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .size(width = 132.dp, height = 37.dp)
                        ){
                            Text(text="Gỡ",color= colorResource(R.color.pink))
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
@Composable
fun ReqTab(navController: NavController){
    Column (
        modifier=Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.friendList.take(2).forEach {friend->
            Row(modifier = Modifier.fillMaxWidth()) {
                GetHinhDaiDienAllFriend(friend.avtFriend.avatarRes)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = friend.nameFriend,
                    )
                    Spacer(Modifier.width(11.dp))
                    Text(text = friend.timeFriend)
                    Row(){
                        val context = LocalContext.current
                        Button(onClick={Toast.makeText(context,"Đã đồng ý kết bạn vơi " + friend.nameFriend,Toast.LENGTH_SHORT).show()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.pink)
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .size(width = 132.dp, height = 37.dp)

                        ){
                            Text(text="Chấp nhận",color=Color.White)
                        }
                        Spacer(Modifier.weight(0.7f))
                        Button(onClick={Toast.makeText(context,"Đã từ chối kết bạn vơi " + friend.nameFriend,Toast.LENGTH_SHORT).show()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .size(width = 132.dp, height = 37.dp)
                        ){
                            Text(text="Từ chối",color= colorResource(R.color.pink))
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
        val context = LocalContext.current
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = {navController.navigate("AllFreindReq")},
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
}
@Composable
fun SendTab(navController: NavController){
    Column (
        modifier=Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.friendList.take(2).forEach { friend->
            Row(modifier = Modifier.fillMaxWidth()) {
                GetHinhDaiDienAllFriend(friend.avtFriend.avatarRes)
                Spacer(Modifier.width(10.dp))
                Column {

                    Row(){Text(
                        text = friend.nameFriend)
                        Spacer(Modifier.weight(1f))
                        Text(text = friend.timeFriend,modifier=Modifier.padding(end = 10.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(){
                        val context = LocalContext.current
                        Button(onClick={Toast.makeText(context,"Đã huy loi moi ket ban voi  " + friend.nameFriend,Toast.LENGTH_SHORT).show()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.white)
                            ),
                            modifier = Modifier
                                // Đặt kích thước cho nút
                                .border(
                                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .fillMaxWidth().height(37.dp)

                        ){
                            Text(text="Hủy",color= colorResource(R.color.pink))
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = {navController.navigate("AllFreindSend")},
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
}
enum class RequestTab( val text:String){
    ReceiveRequest(
        text = "Yêu cầu kết bạn"
    ),
    SentRequest(
        text = "Yêu cầu gửi đi"
    )
}
@Composable
fun GetHinhDaiDienFriend(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}