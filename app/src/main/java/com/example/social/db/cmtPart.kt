package com.example.social.db

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import com.example.social.R
import com.example.social.presentation.viewmodel.CommentViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cmtPart(onDismiss: () -> Unit, posterName: String, postID: String, commentViewModel: CommentViewModel){
    val openBottomSheet by remember { mutableStateOf(true) }

    if(openBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text="Bình luận",style=MaterialTheme.typography.titleLarge)
                    Spacer(modifier=Modifier.height(10.dp))
                }
            }
        ) {
            Divider(
                color = colorResource(R.color.pink ),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            listCmt(posterName, postID, commentViewModel) // Gọi hàm hiển thị danh sách bình luận
        }
    }
}
@Composable
fun listCmt(posterName: String, postID: String, commentViewModel: CommentViewModel){
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        // Đặt LazyColumn để cuộn qua danh sách bình luận
        LazyColumn(modifier = Modifier
            .weight(1f) // Đảm bảo nó chiếm không gian còn lại
            .background(Color.White).fillMaxWidth()) {
            item {
                infoCmt()
            }
        }
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        icon()
        Spacer(Modifier.height(4.dp))
        // TextField nằm bên dưới
        textField(posterName, postID, commentViewModel) // Căn giữa dưới
    }
}
@Composable
fun infoCmt(){
    Spacer(Modifier.height(20.dp))
    userPostDataProvider.cmtList.forEach {cmt->
        Row(){
            getHinhDaiDienCmt(cmt.avtCmt.avatarRes)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = cmt.nameCmt,
                    fontWeight = FontWeight.Bold
                )
                Text(text=cmt.contentCmt)
            }
            Spacer(Modifier.width(5.dp))
            Text(text = cmt.timeCmt)
        }
        Spacer(Modifier.height(41.dp))
    }

}
@Composable
fun getHinhDaiDienCmt(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textField(posterName: String, postID: String, commentViewModel: CommentViewModel){
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    Row() {
        getHinhDaiDienCmt5(R.drawable.avt2)
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            placeholder = { Text("Bình luận về bài viết của $posterName") }, // Sử dụng placeholder
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    commentViewModel.updateComment("cmt", text, postID)
                    Toast.makeText(context,"Đã gửi",Toast.LENGTH_SHORT).show()
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
fun getHinhDaiDienCmt2(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(35.dp))
    )
}
@Composable
fun getHinhDaiDienCmt5(img2 : Int){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp)) // Thêm spacer ở đây để đẩy hình ảnh xuống
        Image(
            painter = painterResource(img2),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(41.dp)
                .clip(RoundedCornerShape(35.dp))
        )
    }
}
@Composable
fun icon(){
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top=7.dp)){
        items(10){ index->//neu lay cac phan tu trong data file thi items(icons.size)
            val icon=icons[0]
            Box(
                modifier= Modifier
                    .size(25.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Green) // Nền màu trắng
            ){
                getHinhDaiDienCmt2(icon.iconRes)

            }
        }
    }
}
