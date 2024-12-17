import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//@Composable
//fun UserManagementScreen(navController: NavController) {
//    val context = LocalContext.current
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Quản lý người dùng") },
//                actions = {
//                    UserOptionsMenu(navController)
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Giao diện chính nơi sử dụng dấu ba chấm", style = MaterialTheme.typography.h6)
//        }
//    }
//}




