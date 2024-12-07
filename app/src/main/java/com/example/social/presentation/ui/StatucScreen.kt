package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@SuppressLint("MutableCollectionMutableState", "SdCardPath")
@Composable
fun StatusScreen(profileViewModel: ProfileViewModel = viewModel(), postViewModel: PostViewModel = viewModel()){
    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value
    profileViewModel.updateImageAvatarUri("avatar")

    val posts = postViewModel.posts.collectAsState().value
    postViewModel.getPosts(Firebase.auth.currentUser!!.uid)

    val context = LocalContext.current
    // ghi nhớ trạng thái chụp ảnh
    var imageBitmap   by remember { mutableStateOf<Bitmap?>(null) }
    // ghi nhớ trạng thái chọn ảnh từ thư viện
    var imagesSelected by remember { mutableStateOf<List<Uri>>(listOf()) }

    // biến lưu trữ ảnh chọn
    val imageUris = remember { mutableStateListOf<Uri>() }
    val imageBitmapSelected = remember { mutableStateListOf<Bitmap?>() }

    var text by remember { mutableStateOf("") }
    val cameraLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
            result->
        if(result.resultCode == android.app.Activity.RESULT_OK)
        {
            imageBitmap = result.data?.extras?.get("data") as Bitmap
            imageBitmap?.let { imageBitmapSelected.add(it) }
            Toast.makeText(context, "Ảnh đã được chụp",Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(context, "Không thể chụp ảnh", Toast.LENGTH_SHORT).show()
    }
    val multipleGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ){ uris: List<Uri> ->
        imagesSelected = uris.toMutableList()
//        imageUris.clear()
        imageUris.addAll(imagesSelected)
    }
//    imageUris.addAll(imagesSelected)
//    if(imageBitmap != null)
//        imageBitmapSelected.add(imageBitmap)

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
            FirstLine2(
                context,
                imageBitmapSelected,
                imageUris,
                text,
                postViewModel,
                posts
            ) // Đặt firstLine2 ở trên
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(R.color.pink)
            )
            Spacer(Modifier.height(15.dp))

            LazyColumn (modifier = Modifier.fillMaxSize()) {
                item{
                    Row(modifier=Modifier.fillMaxWidth().padding(start = 5.dp)) {
                        GetHinhDaiDienChinhSua1(imageAvatar)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = Firebase.auth.currentUser?.displayName.toString(),
                            color = Color.Black,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        )
                    }
                }
                item{
                    TextFieldStatus(text, context){
                        text=it
                    }
                }
                item{
                    key(text) {
                        SetHinh(imageBitmapSelected, imageUris)
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        Spacer(Modifier.height(15.dp))



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
                        painter = painterResource(R.drawable.smile),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick={
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                )
                {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(29.dp)
                    )
                }
                Button(onClick={
                    multipleGalleryLauncher.launch("image/*")
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                )
                {
                    Image(
                        painter = painterResource(R.drawable.upload),
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
fun FirstLine2(
    context: Context, imageBitmaps: MutableList<Bitmap?>, imageUris: MutableList<Uri>, text: String,
    postViewModel: PostViewModel, posts: Map<String, Any>?){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tạo bài đăng", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 29.sp,
            modifier = Modifier.padding(start = 11.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            if (imageUris.isNotEmpty()) {
                postViewModel.saveAndUpdatePostToLocalAndDb(posts, context, imageUris, text)
                Toast.makeText(context, "Bài đăng đã được tạo thành công!", Toast.LENGTH_SHORT).show()
                imageUris.clear()
            }
            if (imageBitmaps.isNotEmpty()) {
                val imgBitmapUris = postViewModel.convertBitmap(context, imageBitmaps)
                postViewModel.saveAndUpdatePostToLocalAndDb(posts, context, imgBitmapUris, text)
                Toast.makeText(context, "Bài đăng đã được tạo thành công!", Toast.LENGTH_SHORT).show()
                imageBitmaps.clear()

            }

        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
        ){
            Image(
                painter = painterResource(R.drawable.email),
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
fun TextFieldStatus(text: String, context: Context, onTextChange: (String) -> Unit){
    Row() {
        TextField(
            value = text,
            onValueChange = { newText -> onTextChange(newText) },
            placeholder = { Text("Bạn đang nghĩ gì ") }, // Sử dụng placeholder
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    Toast.makeText(context,"Đã gửi", Toast.LENGTH_SHORT).show()
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
fun GetHinhDaiDienChinhSua1(img : Uri?){
    Box() {
        Image(
            painter = rememberAsyncImagePainter(img),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp, color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
        )
    }
}

@Composable
fun  SetHinh(imageBitmaps: MutableList<Bitmap?>, imageUris: MutableList<Uri>){
    LazyRow (modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(imageBitmaps.size) { index ->
            val bitmap = imageBitmaps[index]
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(320.dp)
                    .padding(10.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(0.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier.fillMaxSize()
                            .height(400.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        items(imageUris.size) { index ->
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(320.dp)
                    .padding(10.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(0.dp)),
                contentAlignment = Alignment.Center
            ) {
                val uri = imageUris[index]
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.width(400.dp)
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

