package com.example.social.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.social.R
import com.example.social.db.userPostDataProvider
import com.example.social.presentation.viewmodel.FriendViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AllFriendSend(friendViewModel: FriendViewModel = viewModel()){
    val friendSends=friendViewModel.friends.collectAsState().value

    friendViewModel.getFriends(Firebase.auth.currentUser!!.uid, "friendSends")
    val userIdSends = remember(friendSends) {
        friendSends?.mapNotNull { (key, value) ->
            (value as? Map<*, *>)?.get("uid")?.toString()
        }.orEmpty()
    }

    Column(modifier= Modifier.fillMaxSize()){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Lời mời đã gửi",modifier=Modifier.offset(y=10.dp), fontSize = 19.sp)
        }
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.pink)
        )
        Spacer(Modifier.height(10.dp))
        Row(modifier=Modifier.padding(start=6.dp)) {
            Text(
                text = "Lời mời đã gửi", fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = userIdSends.size.toString(), fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color= colorResource(R.color.pink)
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn (modifier=Modifier.fillMaxSize().padding(start=6.dp)){
            item {
                val userInfoList = friendViewModel.userInfo.collectAsState().value
                if (userInfoList.isEmpty() && userIdSends.isNotEmpty()) {
                    friendViewModel.getFriendInfo(userIdSends)
                }
                userInfoList.forEachIndexed{index,userInfo->
                    val userId = userIdSends.getOrNull(index)
                    if(userId!=null) {
                        FriendSendDisplay(userInfo,userId)
                    }
                }
            }
        }
    }
}
