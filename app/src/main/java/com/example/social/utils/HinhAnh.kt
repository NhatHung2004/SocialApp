package com.example.social.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import com.example.social.repository.FirestoreRepo
import com.example.social.repository.ImageRepo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object HinhAnh {
    @Composable
    fun GetHinh(field: String, selectedUri: Uri?, context: Context, callback: @Composable (Uri?) -> Unit) {
        if(selectedUri != null){
            callback(selectedUri)
        }else {
            if (FirestoreRepo.getData("users", field).contains("content://")){ // nếu uri không phù hợp
                // load ảnh từ bộ nhớ
                val uriImage = ImageRepo.loadImageFromInternalStorage(context, field, Firebase.auth.currentUser!!.uid)
                callback(uriImage)
            }
            else {
                callback(Uri.parse(FirestoreRepo.getData("users", field)))
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
        FirestoreRepo.updateData("users", field, selectedUri!!.toString())
        val imagePathAvatar = ImageRepo.saveImageToInternalStorage(
            selectedUri,
            context, field, Firebase.auth.currentUser!!.uid) // sao chép ảnh
        ImageRepo.saveImagePath(context, imagePathAvatar) // lưu đường dẫn
    }

    // xóa ảnh cũ khi người dùng cập nhật ảnh đại diện
    private fun xoaAnh(field: String, context: Context){
        if(ImageRepo.deleteImageFromInternalStorage(context, field, Firebase.auth.currentUser!!.uid)) {
            ImageRepo.removeImagePathFromPreferences(context, field, Firebase.auth.currentUser!!.uid)
        }
    }

    @Composable
    fun checkImgUri(context: Context, field: String) : Uri? {
        return  if (FirestoreRepo.getData("users", field).contains("content://")) {
            ImageRepo.loadImageFromInternalStorage(context, field, Firebase.auth.currentUser!!.uid)
        } else {
            Uri.parse(FirestoreRepo.getData("users", field))
        }
    }

}