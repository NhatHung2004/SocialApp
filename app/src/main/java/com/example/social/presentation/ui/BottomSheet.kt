package com.example.social.presentation.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.FriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendReqToSendBottomSheet(navController: NavController, onDismiss:()->Unit){
    val openBottomSheet by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier= Modifier.fillMaxWidth().height(80.dp),
            dragHandle = {
                Column(
                    modifier= Modifier.fillMaxWidth().background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally

                ){}
            }
        ) {
            Button(onClick = {navController.navigate(Routes.ALL_FRIEND_SEND)},modifier= Modifier.fillMaxSize().padding(start = 0.dp)
                ,colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )){
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
                    Box(
                        modifier = Modifier
                            .size(32.dp) // Kích thước của hình tròn
                            .background(Color.Gray, shape = CircleShape)
                        , contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.man),
                            contentDescription = "avatar",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text="Lời mời đã gửi",color= Color.Black,fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendBottomSheet(friend:Map<String,Any>, isPressed: MutableState<Boolean>, onDismiss:()->Unit, friendViewModel: FriendViewModel = viewModel()){
    val name = "${friend["firstname"]} ${friend["lastname"]}"
    val avatarUri = Uri.parse(friend["avatar"].toString())
    val userId=friend["uid"]

    var openBottomSheet by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {onDismiss()},
            modifier= Modifier.fillMaxWidth().height(150.dp),
            dragHandle = {
                Column(
                    modifier= Modifier.fillMaxWidth().background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = { },
                        modifier = Modifier.fillMaxWidth().padding(start = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )){
                        Row(modifier = Modifier.fillMaxWidth().padding(start = 2.dp)){
                            Image(
                                painter= rememberAsyncImagePainter(avatarUri),
                                contentDescription="avatar",
                                contentScale = ContentScale.Crop,
                                modifier= Modifier
                                    .size(45.dp)
                                    .clip(RoundedCornerShape(35.dp))
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(){
                                Text(text=name
                                    , color = Color.Black
                                    , fontSize = 19.sp
                                    ,modifier= Modifier.padding(top=2.dp))
                            }
                        }
                    }
                }
            }
        ) {
            Column(modifier= Modifier.fillMaxWidth().background(Color.White)) {
                Spacer(Modifier.height(2.dp))
                Divider(
                    color = colorResource(R.color.lightGrey),
                    thickness = 1.dp
                )
                Spacer(Modifier.height(2.dp))
                Button(
                    onClick = { friendViewModel.deleteFriendReq("friends", Firebase.auth.currentUser!!.uid, userId.toString())
                        friendViewModel.deleteFriendReq("friends",  userId.toString(), Firebase.auth.currentUser!!.uid)
                        isPressed.value=true
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 0.dp).height(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp) // Kích thước của hình tròn
                                .background(Color.Gray, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.man),
                                contentDescription = "avatar",
                                colorFilter = ColorFilter.tint(Color.Red),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(text = "Hủy kết bạn với $name", color = Color.Red, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}