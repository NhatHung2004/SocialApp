package com.example.social.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class ImageProcess(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    private val firestoreMethod = FirestoreMethod(firebaseAuth, firestore)

    // dùng đường dẫn được lưu trên firestore để load ảnh local
    suspend fun getImageFromLocal(field: String): Uri? {
        return Uri.parse(firestoreMethod.fetchInfoData("users", field))
    }

    // lưu ảnh vào bộ nhớ
    fun copyImage(selectedUri: Uri, field: String, imagePath: String, context: Context) {
        xoaAnh(field, imagePath, context)
        val imagePathAvatar = saveImageToInternalStorage(
            selectedUri,
            context, field, Firebase.auth.currentUser!!.uid
        ) // sao chép ảnh
        if (imagePathAvatar != null)
            firestoreMethod.updateData("users", field, imagePathAvatar)
        saveImagePath(context, imagePathAvatar) // lưu đường dẫn
    }

    // xóa ảnh cũ khi người dùng cập nhật ảnh đại diện
    private fun xoaAnh(field: String, imagePath: String, context: Context){
        if(deleteImageFromInternalStorage(context, imagePath)) {
            removeImagePathFromPreferences(context, imagePath)
        }
    }

    // lưu 1 ảnh vào local
    private fun saveImageToInternalStorage(uri: Uri?, context: Context, child: String, uid: String): String? {
        return try {
            // Tạo tên file duy nhất cho mỗi ảnh
            val fileName = "image_" + child + "_" + uid + "_"+ System.currentTimeMillis() + ".jpg"
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

    // lưu nhiều ảnh
    fun saveImageToInternalStorage(imageUris: List<Uri>, context: Context, child: String, postId: String): List<Uri> {
        val savedImagePaths = mutableListOf<Uri>()
        for ((index, uri) in imageUris.withIndex()) {
            try {
                val fileName = "image_" + child + "_" + postId + "_" + index + ".jpg"
                val file = File(context.filesDir, fileName)
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                // Thêm đường dẫn của ảnh vào danh sách
                savedImagePaths.add(Uri.parse(file.absolutePath))
            } catch (_: Exception) {
            }
        }
        return savedImagePaths
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

    @SuppressLint("MutatingSharedPrefs")
    fun saveImagePath(context: Context, filePath: List<Uri>) {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)

        // Lấy danh sách đường dẫn hiện tại từ SharedPreferences
        val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Thêm các đường dẫn mới vào danh sách hiện có
        savedPaths.addAll(filePath.map { it.toString() })

        // Lưu lại danh sách cập nhật vào SharedPreferences
        prefs.edit().putStringSet("image_paths", savedPaths).apply()
    }

    private fun deleteImageFromInternalStorage(context: Context, imagePath: String): Boolean {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)
        val savedPaths = prefs.getStringSet("image_paths", emptySet()) ?: emptySet()
        val fileName = imagePath.substringAfterLast("/")
        for(path in savedPaths) {
            val file = File(path)
            if (file.name == fileName) {
                return file.delete()
            }
        }
        return false
    }

    @SuppressLint("SdCardPath")
    fun removeImagePathFromPreferences(context: Context, imagePath: String) {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)
        val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        // Xóa đường dẫn ảnh khỏi `SharedPreferences`
        if (savedPaths.contains(imagePath)) {
            savedPaths.remove(imagePath)
            prefs.edit().putStringSet("image_paths", savedPaths).apply()
        }
    }
}