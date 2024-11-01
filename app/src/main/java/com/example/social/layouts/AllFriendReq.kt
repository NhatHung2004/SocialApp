package com.example.social.layouts

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
fun AllFriendReq(){
    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            ) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Lời mời kết bạn",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = "Lời mời kết bạn", fontSize = 20.sp,
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
                ListReq()
            }
        }
    }
}
@Composable
fun ListReq(){
    Column (
        modifier=Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.friendList.forEach { friend->
            Row(modifier = Modifier.fillMaxWidth()) {
                GetHinhDaiDienFriend(friend.avtFriend.avatarRes)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = friend.nameFriend,
                    )
                    Spacer(Modifier.width(11.dp))
                    Text(text = friend.timeFriend)
                    Row(){
                        val context = LocalContext.current
                        Button(onClick={
                            Toast.makeText(context,"Đã đồng ý kết bạn vơi " + friend.nameFriend,
                                Toast.LENGTH_SHORT).show()},
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
                        Button(onClick={
                            Toast.makeText(context,"Đã từ chối kết bạn vơi " + friend.nameFriend,
                                Toast.LENGTH_SHORT).show()},
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
    }
}
