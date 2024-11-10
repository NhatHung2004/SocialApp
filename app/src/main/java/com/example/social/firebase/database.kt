package com.example.social.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.social.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object Database {

    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore

    fun login(email : String, password : String, navController: NavController, context: Context){
        if(email.isNotEmpty() || password.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if (task.isSuccessful){
                        Toast.makeText(context,"Successfully logged in", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.TAC_VU)
                    }else{
                        Toast.makeText(context,task.exception?.message?:"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun signup(email : String, password : String, ho : String, ten : String, gioiTinh : String, date : String, avatar: Int, backgroundAvatar: Int, navController: NavController, context: Context){
        if(email.isNotEmpty() && password.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful){
                        Toast.makeText(context, "Create account successfully", Toast.LENGTH_SHORT).show()
                        addUser(task.result.user!!, ho, ten, gioiTinh, date, avatar, backgroundAvatar)
                        navController.navigate(Routes.SIGN_IN)
                    }else{
                        Toast.makeText(context, task.exception?.message?:"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    @Composable
    fun getData(collections: String, field: String): String {
        var data by remember { mutableStateOf("") }
        val docRef = db.collection(collections).document(Firebase.auth.currentUser!!.uid)
        docRef.get().addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
            if (documentSnapshot.exists()) {
                data = documentSnapshot.getString(field).toString()
            }
        }
        return data
    }

    fun updateData(collections: String, field: String, value: Uri?) {
        val docRef = db.collection(collections).document(Firebase.auth.currentUser!!.uid)
        docRef.update(field, value)
    }

    fun saveImageToInternalStorage(uri: Uri?, context: Context, child: String, uid: String): String? {
        return try {
            // Tạo tên file duy nhất cho mỗi ảnh
            val fileName = "image_" + child + "_" + uid + ".jpg"
            val file = File(context.filesDir, fileName)

            // Lấy dữ liệu từ URI và sao chép vào file
            val inputStream: InputStream? = uri?.let { context.contentResolver.openInputStream(it) }
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            // Trả về đường dẫn của file đã lưu
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("MutatingSharedPrefs")
    fun saveImagePath(context: Context, filePath: String?) {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)

        // Lấy danh sách đường dẫn hiện tại từ SharedPreferences
        val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Thêm các đường dẫn mới vào danh sách hiện có
        savedPaths.add(filePath)

        // Lưu lại danh sách cập nhật vào SharedPreferences
        prefs.edit().putStringSet("image_paths", savedPaths).apply()
    }

    fun deleteImageFromInternalStorage(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists() && file.delete()
    }

    fun removeImagePathFromPreferences(context: Context, imagePath: String) {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)
        val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Xóa đường dẫn ảnh khỏi `SharedPreferences`
        if (savedPaths.contains(imagePath)) {
            savedPaths.remove(imagePath)
            prefs.edit().putStringSet("image_paths", savedPaths).apply()
        }
    }

    fun loadImageFromInternalStorage(context: Context, child: String, uid: String): Uri? {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)
        val savedPaths = prefs.getStringSet("image_paths", emptySet()) ?: emptySet()
        val fileName = "image_" + child + "_" + uid + ".jpg"
        for(path in savedPaths) {
            val file = File(path)
            if (file.name == fileName) {
                return Uri.fromFile(file)
            }
        }
        return null
    }

    private fun addUser(user: FirebaseUser, ho: String, ten: String, gioiTinh: String, date: String, avatar: Int, backgroundAvatar: Int) {
        val data = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "firstname" to ho,
            "lastname" to ten,
            "sex" to gioiTinh,
            "date" to date,
            "avatar" to Uri.parse("android.resource://com.example.social/drawable/$avatar"),
            "backgroundAvatar" to Uri.parse("android.resource://com.example.social/drawable/$backgroundAvatar")
        )
        val profileUpdates = userProfileChangeRequest {
            displayName = "$ho $ten"
        }
        user.updateProfile(profileUpdates)
        db.collection("users").document(user.uid).set(data)
    }
}
