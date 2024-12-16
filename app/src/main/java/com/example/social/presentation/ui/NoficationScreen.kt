package com.example.social.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social.R
import com.example.social.db.userPostDataProvider
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.NotificationViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel,navController: NavController){

    val context= LocalContext.current
    val notificationContents =context.resources.getStringArray(R.array.notification_contents)

    val notification=notificationViewModel.notifications.collectAsState().value
    notificationViewModel.getNotifications(Firebase.auth.currentUser!!.uid)
    LazyColumn (modifier=Modifier.fillMaxSize()){
        item{
            FirstLine1()
            Spacer(Modifier.height(10.dp))
            Divider(
                color = colorResource(R.color.pink),
                thickness = 1.dp,
            )
        }
        item{
            FriendNotification(notification,notificationViewModel,context, navController,notificationContents)
        }
    }
}
@Composable
fun FirstLine1(){
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.padding(top = 8.dp)){
        Text(
            text = "Thông báo", color = colorResource(R.color.pink),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 11.dp)
        )
        Spacer(modifier = Modifier.width(50.dp))

    }
}
@Composable
fun NotificationList(viewedNotifications: MutableMap<Int, Boolean>) {
    val context= LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        userPostDataProvider.nofList.forEach { notification ->
            val isViewed = remember { mutableStateOf(false) }
            Button(
                onClick={
                    isViewed.value=true
                    Toast.makeText(context,"Đã xem",Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isViewed.value) Color.White else colorResource(R.color.pinkBlur)
                ),
                modifier = Modifier.fillMaxWidth().background
                    (color = if (isViewed.value) Color.White else colorResource(R.color.pinkBlur))

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(color = if (isViewed.value) Color.White else colorResource(R.color.pinkBlur))
                ) {
                    GetHinhDaiDienNof(notification.avtNof.avatarRes)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = buildAnnotatedString {//gop nameNof và contentNof 1 text duy nhất
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.Black)) {
                                    append(notification.nameNof)
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = Color.Black)) {
                                    append(notification.contentNof)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(text = notification.timeNoff,color=Color.Black)
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendNotification(nofInfo:Map<String,Any>?,notificationViewModel: NotificationViewModel,context: Context,navController: NavController,notificationContents: Array<String>){
    if(nofInfo!=null){
        for ((index, entry) in nofInfo.entries.withIndex()) {
            val nofData = entry.value as? Map<*, *>
            val uidUser = nofData?.get("uidUser") as? String
            val uidPost = nofData?.get("uidPost") as? String?
            val contentNof = nofData?.get("content") as? String
            val readState = nofData?.get("readState") as? String
            val timeStamp = nofData?.get("timestamp") as? Long
            var name by remember { mutableStateOf("") }
            var avatar by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                if (uidUser != null) {
                    val nameResutl = notificationViewModel.getName(uidUser)
                    name = nameResutl
                    val avatarResult = notificationViewModel.getAvatar(uidUser).toString()
                    avatar = avatarResult
                }
            }
//                if(uidPost!="") {
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = {
                        if (readState == "notRead") {
                            notificationViewModel.updateReadState(entry.key, "read")
                        }
                        when (contentNof) {
                            notificationContents[0] -> {navController.navigate(Routes.ALL_FRIEND_REQ)}
                            notificationContents[1] -> {navController.navigate(Routes.ALL_FRIEND)}
                            notificationContents[4] -> {navController.navigate("${Routes.POST_FOCUS}/${uidUser}/${uidPost}")}
                            notificationContents[2]->{navController.navigate("${Routes.POST_FOCUS}/${Firebase.auth.currentUser!!.uid}/${uidPost}")}
                            notificationContents[3]->{navController.navigate("${Routes.POST_FOCUS}/${Firebase.auth.currentUser!!.uid}/${uidPost}")}
                        }
                        Toast.makeText(context, "Đã xem", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (readState == "read") MaterialTheme.colorScheme.background else colorResource(
                            R.color.pinkBlur
                        )
                    ),
                    modifier = Modifier.fillMaxWidth().background
                        (color = if (readState == "read") MaterialTheme.colorScheme.background else colorResource(R.color.pinkBlur))

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                color = if (readState == "read") MaterialTheme.colorScheme.background else colorResource(
                                    R.color.pinkBlur
                                )
                            )
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(avatar),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(35.dp))
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = buildAnnotatedString {//gop nameNof và contentNof 1 text duy nhất
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    ) {
                                        append(name)
                                    }
                                    append(" ")
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                        append(contentNof)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(6.dp))
                            val currentDate = timeStamp?.let {
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault()) // Lấy múi giờ hệ thống
                                    .toLocalDate()
                            }
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val formattedDate = currentDate?.format(formatter)
                            if (formattedDate != null) {
                                Text(text = formattedDate, color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GetHinhDaiDienNof(img2 : Int){
    Image(
        painter= painterResource(img2),
        contentDescription="avatar",
        contentScale = ContentScale.Crop,
        modifier= Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(35.dp))

    )
}