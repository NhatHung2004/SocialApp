package com.example.social.presentation.ui.admin

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.social.presentation.viewmodel.AllUserViewModel
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun ThongKe(navController: NavController, allUserViewModel: AllUserViewModel)
{
    val onlineCount = remember { mutableIntStateOf(0) }
    val offlineCount = remember { mutableIntStateOf(0) }

    val users = allUserViewModel.allUsers.collectAsState().value

    fun isOnline(user: Map<String, Any>): Boolean {
        val status = "${user["status"]}"
        return status == "online"
    }

    fun countUsers(users: List<Map<String, Any>>) {
        val online = users.count { user -> isOnline(user) }
        val offline = users.size - online
        onlineCount.intValue = online
        offlineCount.intValue = offline
    }

// Lắng nghe sự thay đổi của `users`
    LaunchedEffect(users) {
        countUsers(users)
    }
    SingleBarChart(onlineCount.intValue,offlineCount.intValue)
}

@Composable
fun SingleBarChart(onlineCount: Int, offlineCount: Int, modifier: Modifier = Modifier) {
    val maxValue = maxOf(onlineCount, offlineCount) // Giá trị tối đa để chuẩn hóa chiều cao cột

    val barWidth = 100.dp
    val chartHeight = 300.dp

    Box(
        contentAlignment = Alignment.Center, // Căn giữa toàn bộ nội dung
        modifier = Modifier
            .fillMaxSize() // Để chiếm toàn bộ màn hình
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 140.dp)
        ) {
            Text(
                text = "Online và Offline",
                modifier = Modifier
                    .padding(bottom = 32.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val barWidthPx = barWidth.toPx()
                val spacing = (canvasWidth - (2 * barWidthPx)) / 3 // Khoảng cách giữa các cột

                val onlineHeight = (onlineCount.toFloat() / maxValue) * canvasHeight
                val offlineHeight = (offlineCount.toFloat() / maxValue) * canvasHeight

                // Vẽ cột Online (màu xanh)
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(spacing, canvasHeight - onlineHeight),
                    size = Size(barWidthPx, onlineHeight)
                )

                // Vẽ cột Offline (màu đỏ)
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(2 * spacing + barWidthPx, canvasHeight - offlineHeight),
                    size = Size(barWidthPx, offlineHeight)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Online", color = Color.Blue)
                    Text(text = "$onlineCount")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Offline", color = Color.Red)
                    Text(text = "$offlineCount")
                }
            }
        }
    }


}



