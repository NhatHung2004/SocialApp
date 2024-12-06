package com.example.social.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.social.R
import com.example.social.data.model.ThemeModel
import com.example.social.presentation.navigation.NavigationTab
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.ThemeViewModel
import com.example.social.ui.theme.SocialTheme
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(navController: NavController, authViewModel: AuthViewModel, themeViewModel: ThemeViewModel){
    val scope = rememberCoroutineScope()//o một coroutine scope để quản lý các coroutine bên trong composable này.
    // Điều này thường được sử dụng để thực hiện các tác vụ bất đồng bộ như thay đổi trang trong HorizontalPager.
    val pagerState = rememberPagerState(pageCount = { HomeTabs.entries.size })//Tạo một trạng thái cho HorizontalPager, số lượng
    // trang được xác định bởi số lượng mục trong enum HomeTabs.
    val selectedIndex = remember { derivedStateOf { pagerState.currentPage } }//Tạo một state được tính toán dựa trên trang hiện tại của HorizontalPager.
    // Khi trang thay đổi, giá trị này sẽ tự động cập nhật.
    val navControllerTab = rememberNavController()
    //Hai dòng lệnh này đóng vai trò quan trọng trong việc theo dõi màn hình hiện tại trong
    // NavController và xác định liệu bạn đang ở màn hình AllFreindReq hay không
    val navBackStackEntry by navControllerTab.currentBackStackEntryAsState()
    val isInAnyFriendReq = navBackStackEntry?.destination?.route in listOf(
        Routes.ALL_FRIEND_REQ,
        Routes.ALL_FRIEND_SEND,
        Routes.ALL_FRIEND,
        Routes.PROFILE_EDIT)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)
    //bien check che do sang toi
    SocialTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ màn hình
            color = MaterialTheme.colorScheme.background, // Màu nền
            contentColor = MaterialTheme.colorScheme.onBackground // Màu chữ
        ){
            Scaffold(//Cau truc xay dung giao dien
                topBar = {//cai nay la top bar chua "TÊN APP"(phần trên cùng)
                    if (selectedIndex.value == HomeTabs.Home.ordinal ) { // Nếu mục Home được chọn thì
                        TopAppBar(
                            title = {
                                Text(
                                    text = "TÊN APP",
                                    color = Color(0xFFFF4081), // Màu chữ hồng
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.ExtraBold

                                )
                            },
                            colors = topAppBarColors(
                                // Thiết lập màu cho TopAppBar
                            )
                        ) // -> TOPBAR mới xuất hiện
                    }
                }
            ) {
                Column(//dùng để sắp các thành phần theo chiều dọc
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding())//Đặt kích thước của Column để chiếm toàn bộ không gian
                    //có sẵn và thêm padding ở trên để tránh che khuất bởi TopAppBar.
                ) {
                    if (!isInAnyFriendReq) {
                        TabRow(//Cấu trúc hiển thị các tab
                            selectedTabIndex = selectedIndex.value,//Chỉ định tab nào đang được chọn dựa trên giá trị selectedIndex.
                            modifier = Modifier.fillMaxWidth(),//Đặt kích thước của TabRow để chiếm toàn bộ chiều rộng.
                            indicator = { tabPositions ->//Thiết lập cách hiển thị chỉ báo cho tab được chọn.(cai ngang mau hong o duoi icon)
                                Box(
                                    Modifier
                                        .tabIndicatorOffset(tabPositions[selectedIndex.value])//Đặt vị trí chỉ báo để nó nằm dưới tab được chọn.
                                        .height(4.dp)
                                        .background(Color(0xFFFF4081)) // Pink indicator
                                )
                            }
                        ) {
                            HomeTabs.entries.forEachIndexed { index, currentTab ->//Duyệt qua tất cả các tab trong enum HomeTabs và lấy cả chỉ số và giá trị của mỗi tab.
                                Tab(//Cấu trúc để tạo mỗi tab.
                                    selected = selectedIndex.value == index,//Xác định xem tab hiện tại có được chọn hay không.
                                    selectedContentColor = Color(0xFFFF4081),
                                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                                    onClick = {//Hành động khi tab được nhấn. Trong hàm này, nó sử dụng coroutine để cuộn đến trang tương ứng với tab được chọn.
                                        scope.launch {
                                            pagerState.animateScrollToPage(currentTab.ordinal)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id = if (selectedIndex.value == index)
                                                    currentTab.selectedIcon else currentTab.unSelectedIcon
                                            ),
                                            contentDescription = "Tab icon",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // HorizontalPager chiếm phần còn lại của màn hình
                    HorizontalPager(//Cấu trúc cho phép người dùng vuốt để chuyển đổi giữa các trang (tabs).
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f) // Sử dụng weight để chiếm không gian còn lại
                    ) {pageIndex->
                        Box(//Chứa nội dung của trang, với nội dung được căn giữa.
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            when(HomeTabs.entries[pageIndex]){
                                HomeTabs.Home -> { HomeScreen(navControllerTab)
                                    // Bạn có thể thay thế nó bằng mã cần thiết để hiển thị nội dung Home.
                                }
                                HomeTabs.Friend -> {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        NavigationTab(navController,Routes.FRIEND)
                                    }
                                }
                                HomeTabs.Status -> {
                                    StatusScreen()
                                }
                                HomeTabs.Notification -> NoficationScreen()
                                HomeTabs.Profile -> {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        NavigationTab(navController,Routes.PROFILE_SCREEN)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

enum class HomeTabs(// Định nghĩa một enum để đại diện cho các tab trong ứng dụng
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val text: String
){
    Home(
        selectedIcon= R.drawable.home,
        unSelectedIcon = R.drawable.home,
        text = "Home"
    ),
    Friend(
        selectedIcon = R.drawable.friends,
        unSelectedIcon = R.drawable.friends,
        text = "Friend"
    ),
    Status(
        selectedIcon = R.drawable.plus,
        unSelectedIcon = R.drawable.plus,
        text = "Status"
    ),
    Notification(
        selectedIcon = R.drawable.alert,
        unSelectedIcon = R.drawable.alert,
        text = "Notification"
    ),
    Profile(
        selectedIcon = R.drawable.user,
        unSelectedIcon = R.drawable.user,
        text = "Profile"
    )
}