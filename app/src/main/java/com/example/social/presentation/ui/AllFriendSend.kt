package com.example.social.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.social.R
import com.example.social.db.userPostDataProvider

@Composable
fun AllFriendSend(){
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
            Text(text="Lời mời đã gửi",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = "Lời mời đã gửi", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "100", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color= colorResource(R.color.pink)
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn (modifier=Modifier.fillMaxSize().padding(start=6.dp)){
            item {
                ListSend()
            }
        }
    }
}
@Composable
fun ListSend(){
    Column (
        modifier=Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.friendList.forEach { friend->
            Row(modifier = Modifier.fillMaxWidth()) {
                GetHinhDaiDienFriend(friend.avtFriend.avatarRes)
                Spacer(Modifier.width(10.dp))
                Column {

                    Row(){
                        Text(text = friend.nameFriend)
                        Spacer(Modifier.weight(1f))
                        Text(text = friend.timeFriend,modifier=Modifier.padding(end = 10.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(){
                        val context = LocalContext.current
                        Button(onClick={Toast.makeText(context,"Đã hủy lời mời kết bạn với " + friend.nameFriend,Toast.LENGTH_SHORT).show()},
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
    }
}
