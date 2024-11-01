package com.example.social.layouts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.social.R
import com.example.social.db.userPostDataProvider

@Composable
fun AllFriend(){
    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Shin Văn Nô",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp
        )
        Spacer(Modifier.height(10.dp))
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                // Đặt kích thước cho nút
                .border(
                    BorderStroke(1.dp, color = colorResource(R.color.pink)),
                    shape = RoundedCornerShape(15.dp)
                )
                .size(width =350.dp, height = 37.dp)
        ) {
            // Sử dụng Box để căn giữa nội dung
            Box(
                // Chiếm toàn bộ không gian của nút
                contentAlignment = Alignment.Center // Căn giữa nội dung
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(R.drawable.searching),
                        contentDescription = "option Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Tìm kiếm bạn bè",
                        color = colorResource(R.color.pink),
                        fontSize = 20.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = "100", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color= colorResource(R.color.pink)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "người bạn", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn (modifier=Modifier.fillMaxSize().padding(start=6.dp)){
            item {
                ListFriend()
            }
        }
    }
}
@Composable
fun ListFriend(){
    Column (
        modifier=Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.friendList.forEach { friend->
            Row(modifier = Modifier.fillMaxWidth()) {
                GetHinhDaiDienAllFriend(friend.avtFriend.avatarRes)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = friend.nameFriend,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        modifier=Modifier.offset(y=10.dp)
                    )
                    Spacer(Modifier.height(11.dp))
                    Text(text ="100 bạn chung")
                }
                Spacer(Modifier.weight(1f))
                Button(onClick={},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                )
                {
                    Image(
                        painter = painterResource(R.drawable.meatballsmenuc),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
@Composable
fun GetHinhDaiDienAllFriend(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}