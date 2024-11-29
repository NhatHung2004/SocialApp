package com.example.social.presentation.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccount(navController: NavController, authViewModel: AuthViewModel, onDismiss: () -> Unit){
    val openBottomSheet by remember { mutableStateOf(true) }
    val currentUser = authViewModel.currentUser.collectAsState().value
    if(openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier= Modifier.height(200.dp),
            dragHandle = {
                Column(
                    modifier=Modifier.fillMaxWidth().background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text="Thêm tài khoản",style= MaterialTheme.typography.titleLarge)
                    Spacer(modifier=Modifier.height(10.dp))
                }
            }


        ) {
            Column(modifier = Modifier.fillMaxSize().background(color= Color.White)) {
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
                                onClick = {
                                    authViewModel.logout()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.pinkBlur)
                                ),
                            ) {
                                Text(text = "Đăng nhập vào tài khoản hiện có"
                                    ,color= Color.Black, style= TextStyle(fontSize = 19.sp)
                                )
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start) {
                            Button(
                                onClick = {navController.navigate(Routes.REGISTER){
                                    popUpTo(navController.currentBackStackEntry?.destination?.route ?: "") {
                                        inclusive = true
                                    }
                                } },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.white)
                                ),
                                border = BorderStroke(2.dp, colorResource(R.color.pink)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Tạo tài khoản mới ", modifier = Modifier.padding(start = 50.dp)
                                    ,color= Color.Black, style= TextStyle(fontSize = 19.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Routes.LOGIN)
        }
    }
}