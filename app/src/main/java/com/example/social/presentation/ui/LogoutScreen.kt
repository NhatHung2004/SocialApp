package com.example.social.presentation.ui

import android.util.Log
import androidx.compose.runtime.collectAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutPart(navController: NavController, profileViewModel: ProfileViewModel,
                authViewModel: AuthViewModel, onDismiss:()->Unit){
    val currentUser = authViewModel.currentUser.collectAsState().value

    val imageAvatar = profileViewModel.imageAvatarUri.collectAsState().value
    val firstname = profileViewModel.firstname.collectAsState().value
    val lastname = profileViewModel.lastname.collectAsState().value

    val openBottomSheet by remember { mutableStateOf(true) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier=Modifier.height(200.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize().background(color=Color.White)) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(10.dp).height(125.dp)
                        .background(Color.White).border(
                            BorderStroke(1.dp, color = colorResource(R.color.pink)),
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth().padding(2.dp),
                            horizontalArrangement = Arrangement.Start) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.white)
                                ),
                            ) {
                                Box() {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageAvatar),
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
                                Spacer(Modifier.width(10.dp))
                                Text(text = "$firstname $lastname"
                                    ,color=Color.Black, style= TextStyle(fontSize = 20.sp)
                                )
                                Spacer(Modifier.weight(1f))
                                Image(
                                    painter=painterResource(R.drawable.check),
                                    contentDescription = "avatar",
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start) {
                            Button(
                                onClick = {
                                    if (currentUser != null) {
                                        authViewModel.logout()
                                        navController.navigate(Routes.LOGIN)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.white)
                                ),
                            ) {
                                Box( modifier = Modifier
                                    .height(75.dp).width(50.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(colorResource(R.color.lightGrey)) ) {
                                    Image(
                                        painter = painterResource(R.drawable.exit),
                                        contentDescription = "avatar",
                                        modifier = Modifier.size(25.dp).align(Alignment.Center)
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                Text(text = "Đăng xuất",
                                    color=Color.Black, style= TextStyle(fontSize = 20.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}