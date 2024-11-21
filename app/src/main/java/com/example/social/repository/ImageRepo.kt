package com.example.social.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ImageRepo {
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

    fun saveImageToInternalStorage(imageUris: List<Uri>, context: Context, child: String, uid: String): List<String> {
        val savedImagePaths = mutableListOf<String>()
        for ((index, uri) in imageUris.withIndex()) {
            try {
                val fileName = "image_" + child + "_" + uid + "_" + index + ".jpg"
                val file = File(context.filesDir, fileName)
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                // Thêm đường dẫn của ảnh vào danh sách
                savedImagePaths.add(file.absolutePath)
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
    fun saveImagePath(context: Context, filePath: List<String>) {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)

        // Lấy danh sách đường dẫn hiện tại từ SharedPreferences
        val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Thêm các đường dẫn mới vào danh sách hiện có
        savedPaths.addAll(filePath)

        // Lưu lại danh sách cập nhật vào SharedPreferences
        prefs.edit().putStringSet("image_paths", savedPaths).apply()
    }

    fun deleteImageFromInternalStorage(context: Context, child: String, uid: String): Boolean {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)
        val savedPaths = prefs.getStringSet("image_paths", emptySet()) ?: emptySet()
        val fileName = "image_" + child + "_" + uid + ".jpg"
        for(path in savedPaths) {
            val file = File(path)
            if (file.name == fileName) {
                return file.delete()
            }
        }
        return false
    }

    @SuppressLint("SdCardPath")
    fun removeImagePathFromPreferences(context: Context, child: String, uid: String) {
        val prefs = context.getSharedPreferences("saved_images", Context.MODE_PRIVATE)
        val savedPaths = prefs.getStringSet("image_paths", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        val imagePath = "/data/user/0/com.example.social/files/" + "image_" + child + "_" + uid + ".jpg"
        // Xóa đường dẫn ảnh khỏi `SharedPreferences`
        if (savedPaths.contains(imagePath)) {
            savedPaths.remove(imagePath)
            prefs.edit().putStringSet("image_paths", savedPaths).apply()
        }
    }

    // load ảnh từ local
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
}