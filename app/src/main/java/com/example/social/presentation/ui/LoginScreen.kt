package com.example.social.presentation.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.ui.theme.SocialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(authViewModel: AuthViewModel = viewModel(), navController: NavController) {
    val currentUser = authViewModel.currentUser.collectAsState().value

    var emailInput by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    val trailingIconPass = if (visiblePassword)
        painterResource(id = R.drawable.unhide)
    else
        painterResource(id = R.drawable.hide)

    val context = LocalContext.current


    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.pink).copy(0.25f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_img),
                contentDescription = "User sign in"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White)
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
                        modifier =  Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                    )
                    TextField(
                        value = emailInput,
                        onValueChange = {
                            emailInput = it
                            isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
                        },
                        placeholder = { Text(text = "abc@gmail.com") },
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription = "Email") },
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
                        onValueChange = {password = it},
                        placeholder = { Text(text = "Password") },
                        visualTransformation = if (visiblePassword) VisualTransformation.None else PasswordVisualTransformation() ,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.password),
                                contentDescription = "Password",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {visiblePassword = !visiblePassword}) {
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
                            authViewModel.login(emailInput, password)
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
                        Text(text = "Forgot", fontSize = 15.sp, color = Color.Black)
                        TextButton(onClick = {}) {
                            Text(
                                text = "password",
                                color = colorResource(R.color.text_color),
                                fontSize = 15.sp
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Create an", fontSize = 15.sp, color = Color.Black)
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
        if (currentUser != null) {
            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()

            // Chuyển về màn hình home
            navController.navigate(Routes.TABS)
        }
    }
}
