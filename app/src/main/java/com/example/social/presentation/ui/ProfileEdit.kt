package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileEdit(navController: NavController, profileViewModel: ProfileViewModel){
    val context = LocalContext.current

    var firstnameState by remember { mutableStateOf("") }
    var lastnameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }

    profileViewModel.getUserInfo()

    firstnameState = profileViewModel.firstname.collectAsState().value
    lastnameState = profileViewModel.lastname.collectAsState().value
    emailState = profileViewModel.email.collectAsState().value

    val imageBackground = profileViewModel.imageBackgroundUri.collectAsState().value
    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value

    var selectedImageUriBackground by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncherBackground = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUriBackground = uri
        }
    )

    var selectedImageUriAvatar by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncherAvatar = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUriAvatar = uri
        }
    )

    Column(){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    navController.navigate(Routes.PROFILE_SCREEN)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Chỉnh sửa trang cá nhân",modifier=Modifier.offset(y=10.dp),
                fontSize = 20.sp, fontWeight = FontWeight.ExtraBold
                ,color= colorResource(R.color.pink)
            )

        }
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        Spacer(Modifier.height(10.dp))
        LazyColumn(modifier=Modifier.fillMaxSize()) {
            item{
                ImageEdit(photoPickerLauncherAvatar,
                    photoPickerLauncherBackground,
                    imageAvatar,
                    imageBackground,
                    selectedImageUriAvatar,
                    selectedImageUriBackground
                )
            }
            item{
                TextFieldHo(firstnameState){
                    firstnameState = it
                }
                Spacer(Modifier.height(20.dp))
            }
            item {
                TextFieldTen(lastnameState){
                    lastnameState = it
                }
                Spacer(Modifier.height(20.dp))
            }
            item{
                TextFieldEmail(emailState){
                    emailState = it
                }
                Spacer(Modifier.height(20.dp))
            }
            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp), // Thêm khoảng cách bên dưới nếu cần
                    contentAlignment = Alignment.Center // Căn giữa nội dung bên trong Box
                ) {
                    Button(
                        onClick = {
                            if(selectedImageUriAvatar != null) {
//                                profileViewModel.copyImage(
//                                    selectedImageUriAvatar!!,
//                                    "avatar",
//                                    imageAvatar.toString(),
//                                    context
//                                )
//                                profileViewModel.updateImageAvatarUri("avatar")
                                profileViewModel.uploadImageToCloudinary(
                                    selectedImageUriAvatar!!,
                                    context, "avatar")
                            }
                            if(selectedImageUriBackground != null) {
//                                profileViewModel.copyImage(
//                                    selectedImageUriBackground!!,
//                                    "backgroundAvatar",
//                                    imageBackground.toString(),
//                                    context
//                                )
//                                profileViewModel.updateImageBackgroundUri("backgroundAvatar")
                                profileViewModel.uploadImageToCloudinary(
                                    selectedImageUriBackground!!,
                                    context, "backgroundAvatar")
                            }
                            profileViewModel.updateUserInfo(firstnameState, lastnameState, emailState)
                            navController.navigate(Routes.PROFILE_SCREEN)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .border(
                                BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .size(width = 320.dp, height = 37.dp)
                    ) {
                        Text(text = "Lưu thay đổi", color = colorResource(R.color.pink))
                    }
                }
            }
        }
    }
}

@Composable
fun ImageEdit(photoPickerLauncherAvatar: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
              photoPickerLauncherBackground: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
              imageAvatarUri: String?,
              imageBackgroundUri: String?,
              selectedImageUriAvatar: Uri?,
              selectedImageUriBackground: Uri?
){
    Column(modifier=Modifier.padding(top=15.dp)){
        Row(modifier=Modifier.fillMaxWidth()){
            Text(text="Ảnh đại diện", fontSize = 25.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    photoPickerLauncherAvatar.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .width(165.dp)
                    .offset(y = (-4).dp)
                    .height(30.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ không gian của Button
                    contentAlignment = Alignment.CenterEnd // Căn trái nội dung
                ) {
                    Text(
                        text = "Chỉnh sửa",
                        color = colorResource(R.color.pink),
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(25.dp))
        if(selectedImageUriAvatar == null) {
            GetHinhDaiDienChinhSua(imageAvatarUri)
        }else {
            GetHinhDaiDienChinhSua(selectedImageUriAvatar.toString())
        }

        Spacer(Modifier.height(25.dp))
        Row(){
            Text(text="Ảnh bìa", fontSize = 25.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    photoPickerLauncherBackground.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .width(165.dp)
                    .offset(y = (-4).dp)
                    .height(30.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ không gian của Button
                    contentAlignment = Alignment.CenterEnd // Căn trái nội dung
                ) {
                    Text(
                        text = "Chỉnh sửa",
                        color = colorResource(R.color.pink),
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        if(selectedImageUriBackground == null) {
            GetHinhBiaChinhSua(imageBackgroundUri)
        }else {
            GetHinhBiaChinhSua(selectedImageUriBackground.toString())
        }
    }
    Spacer(Modifier.height(20.dp))
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TextFieldHo(ho: String, onHoChange: (String) ->Unit){
    OutlinedTextField(
        value = ho,
        onValueChange = { newHo -> onHoChange(newHo) },
        label = { Text("Họ") },  // Nhãn nổi phía trên
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), // Bo góc nhẹ
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.pink),   // Màu viền khi được chọn
            unfocusedBorderColor = Color.Gray  // Màu viền khi không chọn
        )
    )
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TextFieldTen(ten: String, onHoChange: (String) ->Unit){
    OutlinedTextField(
        value = ten,
        onValueChange = { newTen -> onHoChange(newTen) },
        label = { Text("Tên") },  // Nhãn nổi phía trên
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), // Bo góc nhẹ
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.pink),   // Màu viền khi được chọn
            unfocusedBorderColor = Color.Gray  // Màu viền khi không chọn
        )
    )
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TextFieldEmail(email: String, onEmailChange: (String) ->Unit){
    OutlinedTextField(
        value = email,
        onValueChange = { newEmail -> onEmailChange(newEmail) },
        label = { Text("Tên") },  // Nhãn nổi phía trên
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), // Bo góc nhẹ
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.pink),   // Màu viền khi được chọn
            unfocusedBorderColor = Color.Gray  // Màu viền khi không chọn
        )
    )
}

@Composable
fun GetHinhDaiDienChinhSua(img : String?){
    Box(modifier = Modifier.fillMaxSize() ) {
        Image(
            painter = rememberAsyncImagePainter(img),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(100.dp))
                .border(
                    width = 1.dp, color = Color.Black,
                    shape = RoundedCornerShape(100.dp)
                )
                .align(Alignment.Center)
        )
    }
}

@Composable
fun GetHinhBiaChinhSua(img: String?) {
    Image(
        painter = rememberAsyncImagePainter(img),
        contentDescription = "Main Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    )
}