package com.example.social.domain.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.ocpsoft.prettytime.PrettyTime
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun Long.toPrettyTime(): String {
    val prettyTime = PrettyTime(Locale.getDefault()) // Sử dụng ngôn ngữ hệ thống
    return prettyTime.format(Date(this))
}

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

@Composable
fun SetText(content: String, isExpanded: Boolean, maxLenght: Int)
{
    if(content.length > maxLenght)
    {
        if(isExpanded)
        {
            Text(text = content)
        }
        else
        {
            Text(text = content.take(maxLenght) + "...xem thêm")
        }
    }
    else
    {
        Text(text = content)
    }
}

@Composable
fun setText2(content: String, maxLenght: Int):String
{
    if(content.length > maxLenght)
    {
        return content.take(maxLenght) + "..."
    }
    else
    {
        return content
    }
}