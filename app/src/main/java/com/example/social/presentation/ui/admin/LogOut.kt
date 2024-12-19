package com.example.social.presentation.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.social.R
import com.example.social.presentation.navigation.Routes

@Composable
fun LogOut(navController: NavController)
{
    val showDialog = remember { mutableStateOf(false) }
    NavigationDrawerItem(
        label = { Text(text = "Đăng xuất", color = Color.Black,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold) },
        selected = false,
        icon = {Image(painter = painterResource(R.drawable.exit),
            contentDescription = null)},
        onClick = { showDialog.value = true }
    )
    if(showDialog.value){
        YesNoDialog(
            showDialog = showDialog,
            "Đăng xuất",
            "Bạn có muốn thoát không?",
            onClickAction = ({ navController.navigate(Routes.LOGIN){
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true//khi nhấn nút quay lại thì nó out app
            } }))
    }
}
/**
 * Hàm này để hiển thị hộp thoại Yes/No
 * @param showDialog là trạng thái hiển thị hộp thoại
 * @param title là tiêu đề của câu thông báo
 * @param text là nội dung của câu thông báo
 * * @param onClickAction là hành động khi người dùng chọn yes
 */
@Composable
fun YesNoDialog(
    showDialog: MutableState<Boolean>,
    title: String, text: String,
    onClickAction: () -> Unit)
{
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false }, // Đóng hộp thoại khi nhấn ra ngoài
            shape = RoundedCornerShape(16.dp), // Bo tròn các góc hộp thoại
            title = { Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2) // Màu của tiêu đề
            ) },
            text = { Text(text = text,
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            ) },
            confirmButton = {
                TextButton(onClick = {
                    onClickAction()//thực hiện hành động khi chọn yes
                    showDialog.value = false // Đóng hộp thoại sau khi chọn Yes
                },modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(colorResource(R.color.pink), RoundedCornerShape(8.dp))) // Nền và bo tròn nút No)
                { Text("Yes",color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog.value = false // Đóng hộp thoại sau khi chọn No
                }, modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(colorResource(R.color.pink), RoundedCornerShape(8.dp)) // Nền và bo tròn nút Yes
                ) { Text("No",color = Color.White, fontWeight = FontWeight.Bold) }
            }
        )
    }
}