package com.example.social.presentation.ui

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.social.R
import com.example.social.data.model.DataProviderAdmin
import com.example.social.data.model.Friend
import com.example.social.data.model.Post
import com.example.social.domain.utils.convertToTime
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.CommentViewModel
import com.example.social.presentation.viewmodel.FriendRequestViewModel
import com.example.social.presentation.viewmodel.FriendSendViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import com.example.social.presentation.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavController,postViewModel: PostViewModel= viewModel() ,
                commentViewModel: CommentViewModel= viewModel(),friendViewModel: FriendViewModel= viewModel(),friendRequestViewModel: FriendRequestViewModel= viewModel()
                ,friendSendViewModel: FriendSendViewModel= viewModel(),profileViewModel: ProfileViewModel= viewModel()
){
    val currentUser = authViewModel.currentUser.collectAsState().value

    val posts = postViewModel.posts.collectAsState().value
    val friendRequests=friendRequestViewModel.friendRequests.collectAsState().value
    val friends=friendViewModel.friends.collectAsState().value


    val userIdRequests = mutableListOf<Friend>()
    val userIds = mutableListOf<Friend>()


    var emailInput by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    val trailingIconPass = if (visiblePassword)
        painterResource(id = R.drawable.unhide)
    else
        painterResource(id = R.drawable.hide)

    val context = LocalContext.current

    fun isAdmin(email: String, password: String): Boolean {
        return DataProviderAdmin.admin.any { it.username == email && it.password == password }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.pinkBlur)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.pinkBlur))
                .height(250.dp)// Màu hồng
        ) {
            Image(
                painter = painterResource(id = R.drawable.anhnenngoai2),
                contentDescription = "User sign in",
                modifier = Modifier.size(250.dp) // Kích thước tùy chỉnh
                    .align(Alignment.Center) // Căn giữa trong Box
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize().clip(
                    RoundedCornerShape(
                        topStart = 55.dp, // Bo góc ở góc trên trái
                        topEnd = 55.dp,   // Bo góc ở góc trên phải
                        bottomStart = 0.dp, // Không bo góc ở góc dưới trái
                        bottomEnd = 0.dp    // Không bo góc ở góc dưới phải
                    )
                ).border(
                    BorderStroke(1.dp, colorResource(R.color.pink)), // Viền 1dp màu hồng
                    shape = RoundedCornerShape(
                        topStart = 55.dp,
                        topEnd = 55.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .background(colorResource(R.color.white))
                , contentAlignment = Alignment.TopCenter
        ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White).padding(top=70.dp)
        ) {
            Column(
                Modifier
                    .padding(20.dp, 15.dp)
                    .clip(RoundedCornerShape(20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 45.sp,
                    fontFamily = FontFamily(Font(R.font.jaro)),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.pink),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                )
                TextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
                    },
                    placeholder = { Text(text = "abc@gmail.com") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = "Email"
                        )
                    },
                    isError = !isValid,
                    supportingText = {
                        if (!isValid)
                            Text(text = "Invalid Email")
                        else
                            null
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        errorContainerColor = Color.White,
                        errorTextColor = Color.Red
                    )
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = "Password") },
                    visualTransformation = if (visiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.password),
                            contentDescription = "Password",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { visiblePassword = !visiblePassword }) {
                            Icon(
                                painter = trailingIconPass,
                                contentDescription = "Visibility",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        errorContainerColor = Color.White,
                        errorTextColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        if (isAdmin(emailInput, password)) {
                            navController.navigate(Routes.NAR_DRAWER)
                        } else {
                            authViewModel.login(emailInput, password)
                            if (currentUser != null) {
                                postViewModel.getPosts(currentUser.uid)
                                friendViewModel.getFriends(currentUser.uid)
                                friendRequestViewModel.getFriendRequests(currentUser.uid)
                            }
                            if (friendRequests != null) {
                                for ((index, entry) in friendRequests.entries.withIndex()) {
                                    val friendRequestData = entry.value as? Map<*, *>
                                    val userId = friendRequestData?.get("uid") as? String
                                    val timestamp = friendRequestData?.get("timestamp") as Long
                                    if (userId != null) {
                                        userIdRequests.add(Friend(userId, timestamp))
                                    }
                                }
                            }
                            Log.d("FriendRequests", userIdRequests.map { it.uid }.toString())

                            if (friends != null) {
                                for ((index, entry) in friends.entries.withIndex()) {
                                    val friendData = entry.value as? Map<*, *>
                                    val userId = friendData?.get("uid") as? String
                                    val timestamp = friendData?.get("timestamp") as Long
                                    if (userId != null) {
                                        userIds.add(Friend(userId, timestamp))
                                    }
                                }
                            }
                            Log.d("Friends", friends.toString())
                        }
                    },
                    colors = ButtonColors(
                        containerColor = Color.White,
                        contentColor = colorResource(R.color.pink),
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black.copy(0.8f))
                ) {
                    Text(
                        text = "Login",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.jaro)),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(20.dp, 0.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Create an", fontSize = 15.sp)
                    TextButton(onClick = {
                        authViewModel.setRegistrationState(null)
                        navController.navigate("register")
                    }) {
                        Text(
                            text = "account",
                            color = colorResource(R.color.text_color),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
    }
    // Nếu đăng nhập thành công, chuyển sang màn hình home
    LaunchedEffect(currentUser) {
        val firestore = FirebaseFirestore.getInstance()
        if (currentUser != null) {

            if (profileViewModel.checkDelete(currentUser.uid)) {
                val credential = EmailAuthProvider.getCredential(emailInput, password)

                currentUser.reauthenticate(credential)
                    .addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            // Xoá các dữ liệu liên quan trong Firestore
                            val userId = currentUser.uid
                            firestore.collection("users").document(userId).delete()
                                .addOnCompleteListener { deleteDocTask ->
                                    if (deleteDocTask.isSuccessful) {
                                        if (posts != null) {
                                            for ((index, entry) in posts.entries.withIndex()) {
                                                val postData = entry.value as? Map<*, *>
                                                val id = postData?.get("id") as String
                                                commentViewModel.deleteComment(id)
                                            }
                                        }
                                        postViewModel.deletePostDocument(userId)

                                       userIds.map { it.uid }.forEach { uid->
                                           friendViewModel.deleteFriend(uid,userId)
                                       }

                                        userIdRequests.map { it.uid }.forEach { uid->
                                            friendRequestViewModel.deleteFriendReq(uid,userId)
                                        }

                                        friendViewModel.deleteDocument(userId)
                                        friendRequestViewModel.deleteDocument(userId)
                                        friendSendViewModel.deleteDocument(userId)


                                        currentUser.delete()
                                            .addOnCompleteListener { deleteTask ->
                                                if (deleteTask.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Tài khoản và dữ liệu đã bị xoá",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    navController.navigate(Routes.LOGIN)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Xoá tài khoản thất bại",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Xoá dữ liệu Firestore thất bại",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Xác thực thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    // Chuyển về màn hình home
                    navController.navigate(Routes.TABS)
            }
        }
    }
}

