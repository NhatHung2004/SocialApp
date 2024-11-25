package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.social.R
import com.example.social.db.userPostDataProvider

@SuppressLint("UnrememberedMutableState")
@Composable
fun NoficationScreen(){
    val viewedNotifications = mutableStateMapOf<Int, Boolean>()
    LazyColumn (modifier=Modifier.fillMaxSize()){
        item{
            FirstLine1()
            Spacer(Modifier.height(10.dp))
            Divider(
                color = colorResource(R.color.pink),
                thickness = 1.dp,
            )
        }
        item{
            noficationList(viewedNotifications)
        }
    }
}
@Composable
fun FirstLine1(){
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.padding(top = 8.dp)){
        Text(
            text = "Thông báo", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 11.dp)
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
                .size(width = 200.dp, height = 37.dp).padding(end=5.dp)
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
fun noficationList(viewedNotifications: MutableMap<Int, Boolean>) {
    val context= LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.nofList.forEach { notification ->
            val isViewed = remember { mutableStateOf(false) }
            Button(
                onClick={
                    isViewed.value=true
                    Toast.makeText(context,"Đã xem",Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isViewed.value) Color.White else colorResource(R.color.pinkBlur)
                ),
                modifier = Modifier.fillMaxWidth().background
                    (color = if (isViewed.value) Color.White else colorResource(R.color.pinkBlur))

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(color = if (isViewed.value) Color.White else colorResource(R.color.pinkBlur))
                ) {
                    GetHinhDaiDienNof(notification.avtNof.avatarRes)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = buildAnnotatedString {//gop nameNof và contentNof 1 text duy nhất
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.Black)) {
                                    append(notification.nameNof)
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = Color.Black)) {
                                    append(notification.contentNof)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(text = notification.timeNoff,color=Color.Black)
                    }
                }
            }
        }
    }
}
@Composable
fun GetHinhDaiDienNof(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(35.dp))

    )
}