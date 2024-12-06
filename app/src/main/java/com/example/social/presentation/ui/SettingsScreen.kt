package com.example.social.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.social.presentation.viewmodel.ThemeViewModel
import androidx.compose.material3.MaterialTheme

@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel = viewModel()) {
    // Theo dõi trạng thái sáng/tối từ ViewModel
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)

    // Lắng nghe sự thay đổi từ Switch
    LaunchedEffect(isDarkTheme) {
        themeViewModel.toggleTheme(isDarkTheme)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                Text(
                    text = if (isDarkTheme) "Chế độ tối" else "Chế độ sáng",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { isChecked ->
                        // Chỉ thay đổi nếu giá trị khác
                        if (isChecked != isDarkTheme) {
                            themeViewModel.toggleTheme(isChecked)
                        }
                    }
                )
            }
        }
    }
}
