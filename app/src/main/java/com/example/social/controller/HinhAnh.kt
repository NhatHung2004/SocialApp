package com.example.social.controller

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import com.example.social.firebase.Database
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object HinhAnh {
    @Composable
    fun GetHinh(field: String, selectedUri: Uri?, context: Context, callback: @Composable (Uri?) -> Unit) {
        if(selectedUri != null){
            callback(selectedUri)
        }else {
            if (Database.getData("users", field).contains("content://")){ // nếu uri không phù hợp
                // load ảnh từ bộ nhớ
                val uriImage = Database.loadImageFromInternalStorage(context, field, Firebase.auth.currentUser!!.uid)
                callback(uriImage)
            }
            else {
                callback(Uri.parse(Database.getData("users", field)))
            }
        }
    }

    @Composable
    fun HienThiAnh(field: String, selectedUri: Uri?, context: Context, callback: @Composable (Uri?) -> Unit) {
        GetHinh(field, selectedUri, context) { uri ->
            callback(uri)
        }
    }

    // lưu ảnh vào bộ nhớ
    fun saoChepAnh(selectedUri: Uri?, field: String, context: Context) {
        xoaAnh(field, context)
        Database.updateData("users", field, selectedUri!!.toString())
        val imagePathAvatar = Database.saveImageToInternalStorage(
            selectedUri,
            context, field, Firebase.auth.currentUser!!.uid) // sao chép ảnh
        Database.saveImagePath(context, imagePathAvatar) // lưu đường dẫn
    }

    // xóa ảnh cũ khi người dùng cập nhật ảnh đại diện
    private fun xoaAnh(field: String, context: Context){
        if(Database.deleteImageFromInternalStorage(context, field, Firebase.auth.currentUser!!.uid)) {
            Database.removeImagePathFromPreferences(context, field, Firebase.auth.currentUser!!.uid)
        }
    }

    @Composable
    fun checkImgUri(context: Context, field: String) : Uri? {
        return  if (Database.getData("users", field).contains("content://")) {
            Database.loadImageFromInternalStorage(context, field, Firebase.auth.currentUser!!.uid)
        } else {
            Uri.parse(Database.getData("users", field))
        }
    }

}