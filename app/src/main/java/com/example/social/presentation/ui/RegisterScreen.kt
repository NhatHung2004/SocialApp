package com.example.social.presentation.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.social.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(authViewModel: AuthViewModel = viewModel(), navController: NavController) {
    // Lắng nghe trạng thái đăng ký
    val registrationState = authViewModel.registrationState.collectAsState().value

    val context = LocalContext.current

    var ho by remember { mutableStateOf("") }
    var ten by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }
    var visibleConfirmPassword by remember { mutableStateOf(false) }
    var isValidEmail by remember { mutableStateOf(true) }
    var isValidPassword by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    var sex by remember { mutableStateOf("") }
    val sexOptions = listOf("Nam", "Nữ", "Khác")

    val avatar = if (sex == "Nam") R.drawable.nam else if (sex == "Nữ") R.drawable.nu else R.drawable.khac
    val backgroundAvatar = R.drawable.background

    var date by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date = "$dayOfMonth/${month + 1}/$year"
        }, year, month, day
    )

    val trailingIconPass = if (visiblePassword)
        painterResource(id = R.drawable.unhide)
    else
        painterResource(id = R.drawable.hide)

    val trailingIconPassConfirm = if (visibleConfirmPassword)
        painterResource(id = R.drawable.unhide)
    else
        painterResource(id = R.drawable.hide)

    Column(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.pinkBlur))
            .padding(top = 20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp, 20.dp)
        ) {
            IconButton(onClick = {
                navController.navigate("login")
            }) {
                Icon(
                    painter = painterResource(R.drawable.arrow5),
                    tint = colorResource(R.color.text_color),
                    contentDescription = "Back",
                    modifier = Modifier.size(33.dp)
                )
            }
            Text(
                text = "Sign Up",
                fontSize = 45.sp,
                fontFamily = FontFamily(Font(R.font.jaro)),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.pink),
                modifier =  Modifier.padding(start = 10.dp).offset(x = 0.dp, y = (-5).dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // HO
                Box{
                    OutlinedTextField(
                        value = ho,
                        onValueChange = {ho = it},
                        label = { Text(text = "Họ") },
                        textStyle = TextStyle(fontSize = 20.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            errorContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            errorTextColor = Color.Red
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.nameplate),
                                tint = colorResource(R.color.text_color),
                                contentDescription = "Họ",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier =  Modifier.width(160.dp),
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .offset(42.dp, 20.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(0.5f)
                    )
                }
                // TEN
                Box{
                    OutlinedTextField(
                        value = ten,
                        onValueChange = {ten = it},
                        label = { Text(text = "Tên") },
                        textStyle = TextStyle(fontSize = 20.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            errorContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            errorTextColor = Color.Red
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.nameplate),
                                tint = colorResource(R.color.text_color),
                                contentDescription = "Họ",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier =  Modifier.width(160.dp),
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .offset(42.dp, 20.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp).fillMaxWidth()
            ) {
                // EMAIL
                Box {
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = {
                            emailInput = it
                            isValidEmail = it.contains("@gmail.com")
                        },
                        label = { Text(text = "abc@gmail.com") },
                        textStyle = TextStyle(fontSize = 20.sp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.mail),
                                contentDescription = "Email",
                                tint = colorResource(R.color.text_color),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        isError = !isValidEmail,
                        supportingText = {
                            if (!isValidEmail)
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
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            errorContainerColor = Color.White,
                            errorTextColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .offset(42.dp, 20.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                // PASSWORD
                Box {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {password = it},
                        label = { Text(text = "Password") },
                        textStyle = TextStyle(fontSize = 20.sp),
                        visualTransformation = if (visiblePassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.password),
                                contentDescription = "Password",
                                tint = colorResource(R.color.text_color),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {visiblePassword = !visiblePassword}) {
                                Icon(
                                    painter = trailingIconPass,
                                    contentDescription = "password",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            errorContainerColor = Color.White,
                            errorTextColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .offset(42.dp, 20.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                // CONFIRM PASSWORD
                Box {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            isValidPassword = it == password
                        },
                        isError = !isValidPassword,
                        supportingText = {
                            if (!isValidPassword)
                                Text(text = "Mật khẩu không trùng khớp")
                            else
                                null
                        },
                        label = { Text(text = "Nhập lại password") },
                        textStyle = TextStyle(fontSize = 20.sp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.confirmpass),
                                contentDescription = "Password",
                                tint = colorResource(R.color.text_color),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        singleLine = true,
                        visualTransformation = if (visibleConfirmPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {visibleConfirmPassword = !visibleConfirmPassword}) {
                                Icon(
                                    painter = trailingIconPassConfirm,
                                    contentDescription = "VisibilityConfirm",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            errorContainerColor = Color.White,
                            errorTextColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .offset(42.dp, 20.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                // DATE
                Box {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        label = { Text(text = "Ngày sinh") },
                        textStyle = TextStyle(fontSize = 20.sp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Calendar",
                                tint = colorResource(R.color.text_color),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                datePickerDialog.show()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pen),
                                    contentDescription = "VisibilityConfirm",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            errorContainerColor = Color.White,
                            errorTextColor = Color.Red
                        ),
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().clickable {
                            datePickerDialog.show()
                        },
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(30.dp)
                            .offset(42.dp, 20.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                // SEX
                Box {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = sex,
                            onValueChange = {},
                            label = { Text(text = "Giới tính") },
                            textStyle = TextStyle(fontSize = 20.sp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.sex),
                                    contentDescription = "Sex",
                                    tint = colorResource(R.color.text_color),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.Black,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                errorContainerColor = Color.White,
                                errorTextColor = Color.Red
                            ),
                            readOnly = true,
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                        )
                        VerticalDivider(
                            modifier = Modifier
                                .height(30.dp)
                                .offset(42.dp, 20.dp),
                            thickness = 1.dp,
                            color = Color.Gray.copy(0.5f)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            sexOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option) },
                                    onClick = {
                                        sex = option
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            authViewModel.register(emailInput, password, ho, ten, sex, date,
                                "android.resource://com.example.social/drawable/$avatar",
                                "android.resource://com.example.social/drawable/$backgroundAvatar")
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
                            text = "ĐĂNG KÝ",
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.jaro)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(20.dp, 0.dp)
                        )
                    }
                }
            }
        }
    }

    // Nếu đăng ký thành công, chuyển sang màn hình login
    LaunchedEffect(registrationState) {
        if (registrationState == true) {
            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()

            // Chuyển về màn hình đăng nhập sau khi đăng ký thành công
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }
}