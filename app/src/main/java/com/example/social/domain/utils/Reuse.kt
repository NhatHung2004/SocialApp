package com.example.social.domain.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun convertToTime(time:Long):String{
    val currentDate = Instant.ofEpochMilli(time)
        .atZone(ZoneId.systemDefault()) // Lấy múi giờ hệ thống
        .toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate = currentDate.format(formatter)
    return formattedDate
}