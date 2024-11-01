package com.example.social.layouts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.NavController
import com.example.social.R
import androidx.compose.ui.platform.LocalContext
import com.example.social.firebase.Database
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    var emailInput by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    val trailingIconPass = if (visiblePassword)
        painterResource(id = R.drawable.unhide)
    else
        painterResource(id = R.drawable.hide)
    Column(
        Modifier
            .padding(20.dp, 15.dp)
            .clip(RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sign up",
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
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
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
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color.Black,
                errorContainerColor = Color.White,
                errorTextColor = Color.Red
            )
        )
        TextField(
            value = displayName,
            onValueChange = {displayName = it},
            placeholder = { Text(text = "User name") },
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
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color.Black,
                errorContainerColor = Color.White,
                errorTextColor = Color.Red
            )
        )
        Button(onClick = {
            Database.signup(emailInput, password, displayName, navController, context)
        }) { Text(text = "Sign up") }
    }
}