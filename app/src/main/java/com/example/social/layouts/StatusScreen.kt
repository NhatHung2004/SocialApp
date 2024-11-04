package com.example.social.layouts

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.social.R

@Composable
fun StatusScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize() // Đảm bảo Column chiếm toàn bộ kích thước có sẵn
            .padding(top = 16.dp), // Căn lên trên
        verticalArrangement = Arrangement.SpaceBetween // Căn đều giữa các thành phần
    ) {
        // Column chứa firstLine2 và textFieldStatus
        Column(
            modifier = Modifier
                .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                .weight(1f), // Chiếm không gian còn lại
            verticalArrangement = Arrangement.Top, // Căn đều theo chiều dọc
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FirstLine2() // Đặt firstLine2 ở trên
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(R.color.pink)
            )
            Spacer(Modifier.height(15.dp))

            // Thêm TextField trực tiếp vào Column
            TextFieldStatus() // Đặt textFieldStatus ở dưới
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        // Box ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                .height(60.dp) // Thiết lập chiều cao cho Box mới
                .background(color = Color.White), // Bạn có thể đổi màu theo ý thích
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)){
                Button(onClick={},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                )
                {
                    Image(
                        painter = painterResource(R.drawable.home),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(29.dp)
                    )
                }
                Spacer(Modifier.width(50.dp))
                Button(onClick={},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                )
                {
                    Image(
                        painter = painterResource(R.drawable.plus),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(29.dp)
                    )
                }
                Spacer(Modifier.width(50.dp))
                Button(onClick={},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                )
                {
                    Image(
                        painter = painterResource(R.drawable.searching),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(29.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun FirstLine2(){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tạo bài đăng", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 29.sp,
            modifier = Modifier.padding(start = 11.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
        ){
            Image(
                painter = painterResource(R.drawable.searching),
                contentDescription = "option Icon",
                modifier = Modifier
                    .size(29.dp)
                    .offset(y = (-5).dp)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldStatus(){
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    Row() {
        GetHinhDaiDienStatus(R.drawable.avt2)
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            placeholder = { Text("Bạn đang nghĩ gì ") }, // Sử dụng placeholder
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    Toast.makeText(context,"Đã gửi", Toast.LENGTH_SHORT).show()
                    text=""
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
        )
    }
}
@Composable
fun GetHinhDaiDienStatus(img2 : Int){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp)) // Thêm spacer ở đây để đẩy hình ảnh xuống
        Image(
            painter = painterResource(img2),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(35.dp))
        )
    }
}
