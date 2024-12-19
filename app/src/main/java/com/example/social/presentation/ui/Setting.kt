package com.example.social.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.example.social.presentation.viewmodel.ThemeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Setting(themeViewModel: ThemeViewModel,profileViewModel: ProfileViewModel, navController: NavController){

    val context= LocalContext.current

    var mode by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val mode1=profileViewModel.getMode(Firebase.auth.currentUser!!.uid)
        mode = mode1.toString()
    }

    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)

    // Lắng nghe sự thay đổi từ Switch
    LaunchedEffect(isDarkTheme) {
        themeViewModel.toggleTheme(isDarkTheme)
    }
    var isModeratorEnabled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Cài đặt",modifier=Modifier.offset(y=10.dp),fontSize = 19.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(5.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(5.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item{
                Spacer(Modifier.height(5.dp))
                Row(modifier=Modifier.fillMaxWidth()){
                    Image(
                        painter = painterResource(R.drawable.brightnessandcontrast),
                        contentDescription = "Back",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.size(20.dp).offset(x=5.dp)
                    )
                    Spacer(Modifier.width(15.dp))
                    Text(text="Giao diện sáng tối", fontSize = 22.sp)
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isChecked ->
                            // Chỉ thay đổi nếu giá trị khác
                            if (isChecked != isDarkTheme) {
                                themeViewModel.toggleTheme(isChecked)
                            }
                        }
                    )
                }
                Spacer(Modifier.height(5.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(10.dp))
            }

            item{
                Row(modifier=Modifier.fillMaxWidth()){
                    Image(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "Back",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(20.dp).offset(x=5.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                    Spacer(Modifier.width(15.dp))
                    Text(text="Chức năng kiểm duyệt", fontSize = 22.sp)
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = isModeratorEnabled,
                        onCheckedChange = { isChecked ->
                            if(mode=="true") {
                                isModeratorEnabled = isChecked
                            }
                            else{
                                Toast.makeText(context, "Bạn không phải là người kiểm duyệt", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                if (isModeratorEnabled) {
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = {navController.navigate(Routes.POST_FOR_MODERATOR)},modifier=Modifier.fillMaxWidth()
                    , colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),) {
                        Row() {
                            Spacer(Modifier.width(20.dp))
                            Text(
                                text = "Danh sách bài viết",
                                fontSize = 19.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(start = 25.dp)
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Image(
                            painter = painterResource(R.drawable.rightarrow),
                            contentDescription = "Back",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            }
        }
    }
}