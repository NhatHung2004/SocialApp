package com.example.social.presentation.ui.admin

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social.R
import com.example.social.presentation.navigation.Routes
import com.example.social.presentation.viewmodel.AllUserViewModel
import com.example.social.presentation.viewmodel.AuthViewModel
import com.example.social.presentation.viewmodel.FriendViewModel
import com.example.social.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NarDrawer(navController: NavController, authViewModel: AuthViewModel,
              friendViewModel: FriendViewModel = viewModel(),
              allUserViewModel: AllUserViewModel = viewModel(),
              postViewModel: PostViewModel = viewModel()
)
{
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext
    val navControllerNarDrawer = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(1) }
    // null ban đầu có nghĩa là không item nào được chọn
    // Hàm xử lý khi item trong NavigationDrawer được nhấn
    fun onItemClick(itemId: Int) {
        selectedItem = itemId
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent =
        {
            ModalDrawerSheet()
            {
                //Trang chủ
                Box(modifier = Modifier.fillMaxWidth().height(65.dp)
                    .background(colorResource(R.color.pink)),
                    contentAlignment = Alignment.Center)
                {
                    Text(text = "Trang chủ",color = Color.White
                        ,fontSize = 30.sp,fontWeight = FontWeight.Bold,
                        modifier = Modifier.wrapContentSize(Alignment.Center).clickable
                            {
                                coroutineScope.launch {drawerState.close()}
                                navControllerNarDrawer.navigate(Routes.THONG_KE)
                                {popUpTo(0)}
                                onItemClick(1)
                            }
                        )
                }
                Divider()
                Spacer(modifier = Modifier.fillMaxWidth().height(50.dp))
                //Quản lý
                NavigationDrawerItem(
                    label =
                    {Text(text = "Quản lý", color = Color.Black,
                        fontSize = 30.sp, fontWeight = FontWeight.Bold)},
                    selected = false,
                    icon = {Image(painter = painterResource(R.drawable.quanly),
                        contentDescription = null)},
                    onClick ={}
                )
                ////QL user
                NavigationDrawerItem(
                    label =
                    {
                        Text(text = "Quản lý người dùng",
                            color = Color.Black, fontSize = 20.sp)
                    },
                    selected = selectedItem == 2,
                    icon = {},
                    modifier = Modifier.padding(start = 50.dp),
                    onClick =
                    {
                        coroutineScope.launch { drawerState.close() }
                        navControllerNarDrawer.navigate(Routes.QUAN_LI_USER)
                        {popUpTo(0)}
                        onItemClick(2)
                    }
                )
                ////QL bài đăng
                NavigationDrawerItem(
                    label = { Text(text = "Quản lý bài đăng",
                        color = Color.Black, fontSize = 20.sp) },
                    selected = selectedItem == 3,
                    icon = {},
                    modifier = Modifier.padding(start = 50.dp),
                    onClick =
                    {
                        coroutineScope.launch { drawerState.close() }
                        navControllerNarDrawer.navigate(Routes.QUAN_LI_POST)
                        {popUpTo(0)}
                        onItemClick(3)
                    }
                )
                ////QL mối quan hệ
                NavigationDrawerItem(
                    label = { Text(text = "Quản lý mối quan hệ",
                        color = Color.Black, fontSize = 20.sp) },
                    selected = selectedItem == 4,
                    icon = {},
                    modifier = Modifier.padding(start = 50.dp),
                    onClick =
                    {
                        coroutineScope.launch { drawerState.close() }
                        navControllerNarDrawer.navigate(Routes.QUAN_LI_MOI_QH)
                        {popUpTo(0)}
                        onItemClick(4)
                    }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(50.dp))
                //Tài khoản
                NavigationDrawerItem(
                    label = { Text(text = "Tài khoản",
                        color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold) },
                    selected = false,
                    icon = { Image(painter = painterResource(R.drawable.user),
                        contentDescription = null) },
                    onClick ={}
                )
                ////Chỉnh sửa tài khoản
                NavigationDrawerItem(
                    label = { Text(text = "Chỉnh sửa",
                        color = Color.Black, fontSize = 20.sp) },
                    selected = selectedItem == 5,
                    icon = {},
                    modifier = Modifier.padding(start = 50.dp),
                    onClick =
                    {
                        coroutineScope.launch { drawerState.close() }
                        navControllerNarDrawer.navigate(Routes.CHINH_SUA)
                        {popUpTo(0)}
                        onItemClick(5)
                    }
                )
                ////Đăng xuất tài khoản
                LogOut(navController)
            }
        }
        )
    {
        Scaffold(
            topBar =
            {
                val coroutineScope1= rememberCoroutineScope()
                TopAppBar(
                    title = { Text(text = GetTitle(selectedItem),
                        style = TextStyle(fontSize = 30.sp),
                        color = colorResource(R.color.pink)
                    ) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary),
                    navigationIcon =
                    {
                        IconButton(onClick =
                        {coroutineScope1.launch { drawerState.open() }})
                        {
                            Image(painterResource(R.drawable.menubar), contentDescription = null)
                        }

                    },
                    actions =
                    {
                        IconButton(
                            onClick = { Toast.makeText(context, "", Toast.LENGTH_SHORT).show()})
                        {
                            Image(painterResource(R.drawable.user), contentDescription = null)
                        }
                    }
                )
            }
        ){

           NavHost(navController = navControllerNarDrawer, startDestination = Routes.THONG_KE)
           {
               composable(Routes.THONG_KE){ ThongKe(navControllerNarDrawer,allUserViewModel) }
               composable(Routes.QUAN_LI_USER){ QuanLyUser(navControllerNarDrawer, authViewModel,
                    allUserViewModel, friendViewModel, postViewModel)}
               composable(Routes.QUAN_LI_POST){ QuanLyBaiDang(navControllerNarDrawer,
                   allUserViewModel,postViewModel )}
               composable(Routes.QUAN_LI_MOI_QH){ QuanLyMQH(navControllerNarDrawer,
                    allUserViewModel, friendViewModel)}
//               composable(Routes.CHINH_SUA){ ChinhSua(navControllerNarDrawer, id)}
           }
        }
    }
}
@Composable
fun GetTitle(stt: Int): String
{
    return when (stt) {
        1 -> "Thống kê"
        2 -> "Quản lí người dùng"
        3 -> "Quản lí bài viết"
        4 -> "Quản lí mối quan hệ"
        5 -> "Chỉnh sửa"
        else -> "Đăng xuất"
    }
}




