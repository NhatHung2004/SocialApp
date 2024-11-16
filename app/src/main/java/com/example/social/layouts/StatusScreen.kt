package com.example.social.layouts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.runtime.getValue
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
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.firebase.Database
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.io.File

@Composable
fun StatusScreen(){
    val context = LocalContext.current
    var imageBitmaps by remember { mutableStateOf<List<Bitmap>>(listOf()) }
    var imageUris by remember { mutableStateOf<List<Uri>>(listOf()) } // Lưu trữ URI ảnh đã chọn
    var text by remember { mutableStateOf("") }
    val cameraLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
            result->
        if(result.resultCode == android.app.Activity.RESULT_OK)
        {
            val bitmap=result.data?.extras?.get("data") as Bitmap
            imageBitmaps=imageBitmaps+bitmap
            Toast.makeText(context, "Ảnh đã được chụp",Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(context, "Không thể chụp ảnh", Toast.LENGTH_SHORT).show()
    }
    val mutipleGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ){ uris: List<Uri> ->
        imageUris=uris.toMutableList()
    }



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
                imageBitmaps = imageBitmaps,
                imageUris = imageUris,
                text = text,
                onPostCreated = {
                    imageUris = listOf()
                    imageBitmaps = listOf()
                    text = ""
                },
                onSuccess = { postId ->
                    Log.d("Post Created", "Post ID: $postId")
                }
            ) // Đặt firstLine2 ở trên
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(R.color.pink)
            )
            Spacer(Modifier.height(15.dp))

            LazyColumn (modifier = Modifier.fillMaxSize()) {
                item{
                    Row(modifier=Modifier.fillMaxWidth().padding(start = 5.dp)) {
                        val uriImage = Database.loadImageFromInternalStorage(
                            context,
                            "avatar", Firebase.auth.currentUser!!.uid
                        )
                        GetHinhDaiDienChinhSua1(uriImage)
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
                    SetHinh(imageBitmaps,imageUris)
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
                        painter = painterResource(R.drawable.home),
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
                        painter = painterResource(R.drawable.plus),
                        contentDescription = "option Icon",
                        modifier = Modifier
                            .size(29.dp)
                    )
                }
                Button(onClick={
                    mutipleGalleryLauncher.launch("image/*")
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
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
fun FirstLine2(context: Context, imageBitmaps: List<Bitmap>, imageUris: List<Uri>, text:String, onPostCreated: () -> Unit, onSuccess: (String) -> Unit){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tạo bài đăng", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 29.sp,
            modifier = Modifier.padding(start = 11.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            if(imageUris.isNotEmpty()) {
                Database.addPost(Firebase.auth.currentUser!!.uid, text, imageUris) { postId ->
                    savePostImageToInternalStorage(imageUris, context, "imageUris", postId)
                    saveImagePath1(context,imageUris.map { it.toString() })
                    onPostCreated()
                    Toast.makeText(context, "Bài đăng đã được tạo thành công!", Toast.LENGTH_SHORT).show()
                    onSuccess(postId)
                }
            }
        },
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
        AsyncImage(
            model = img,
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
fun SetHinh(imageBitmaps: List<Bitmap>,imageUris: List<Uri>){
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
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier.fillMaxSize()
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )
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

@SuppressLint("MutatingSharedPrefs")
fun saveImagePath1(context: Context, filePath: List<String>) {
    val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)

    // Lấy danh sách đường dẫn hiện tại từ SharedPreferences
    val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

    // Thêm các đường dẫn mới vào danh sách hiện có
    savedPaths.addAll(filePath)

    // Lưu lại danh sách cập nhật vào SharedPreferences
    prefs.edit().putStringSet("image_paths", savedPaths).apply()
}

fun savePostImageToInternalStorage(imageUris: List<Uri>, context: Context, child: String, uid: String): List<String> {
    val savedImagePaths = mutableListOf<String>()
    for ((index, uri) in imageUris.withIndex()) {
        try {
            val fileName = "image_" + child + "_" + uid + "_" + index + ".jpg"
            val file = File(context.filesDir, fileName)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Thêm đường dẫn của ảnh vào danh sách
            savedImagePaths.add(file.absolutePath)
        } catch (_: Exception) {
        }
    }
    return savedImagePaths
}
