package com.example.social.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.UserRepo
import com.example.social.domain.utils.FirestoreMethod
import com.example.social.domain.utils.ImageProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val imageProcess = ImageProcess(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val firestoreMethod = FirestoreMethod(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _imageAvatarUri = MutableStateFlow<String?>(null)
    private val _imageBackgroundUri = MutableStateFlow<String?>(null)
    private val _firstname = MutableStateFlow("")
    private val _lastname = MutableStateFlow("")
    private val _email = MutableStateFlow("")

    val imageAvatarUri: StateFlow<String?> get() = _imageAvatarUri
    val imageBackgroundUri: StateFlow<String?> get() = _imageBackgroundUri
    val firstname: StateFlow<String> get() = _firstname
    val lastname: StateFlow<String> get() = _lastname
    val email: StateFlow<String> get() = _email

    fun getUserInfo() {
        viewModelScope.launch {
            _firstname.value = userRepo.fetchUserInfo("firstname").toString()
            _lastname.value = userRepo.fetchUserInfo("lastname").toString()
            _email.value = userRepo.fetchUserInfo("email").toString()
            _imageAvatarUri.value = userRepo.fetchUserInfo("avatar").toString()
            _imageBackgroundUri.value = userRepo.fetchUserInfo("backgroundAvatar").toString()
        }
    }

    suspend fun checkDelete(uid: String):Boolean{
        return  if(firestoreMethod.fetchInfoData("users", "deleted",uid) == "false") false
                 else true
    }

    suspend fun getMode(uid:String): String? {
        return firestoreMethod.fetchInfoData("users", "mode",uid)
    }

    //kiểm tra xem email trong authen có khác với email trong document không
    //tham số truyền vào là email authen
    //nếu email trong document khác trong authen thì trả về false
    suspend fun checkEmail(uid:String, email: String): Boolean{
        return firestoreMethod.fetchInfoData("users", "email",uid) != email
    }

    suspend fun getEmailFromDocment(uid:String): String? {
        return firestoreMethod.fetchInfoData("users", "email",uid)
    }

    fun getUserInfoFromId(userId:String) {
        viewModelScope.launch {
            _firstname.value = userRepo.fetchUserInfoFromUid("firstname",userId).toString()
            _lastname.value = userRepo.fetchUserInfoFromUid("lastname",userId).toString()
            _email.value = userRepo.fetchUserInfoFromUid("email",userId).toString()
            _imageAvatarUri.value = userRepo.fetchUserInfoFromUid("avatar",userId).toString()
            _imageBackgroundUri.value = userRepo.fetchUserInfoFromUid("backgroundAvatar",userId).toString()
        }
    }

    fun uploadImageToCloudinary(uri: Uri, context: Context, field: String) {
        viewModelScope.launch {
            val imagePath = imageProcess.uploadImageToCloudinary(uri, context)
            firestoreMethod.updateData("users", field, imagePath)
            if (field == "avatar") {
                _imageAvatarUri.value = imagePath
            } else if (field == "backgroundAvatar") {
                _imageBackgroundUri.value = imagePath
            }
        }
    }

    fun updateUserInfo(firstname: String, lastname: String, email: String) {
        viewModelScope.launch {
            userRepo.updateUserInfoToFirestore(firstname, lastname, email)
            _firstname.value = firstname
            _lastname.value = lastname
            _email.value = email
        }
    }

    //Hàm cho admin dùng
    fun updateEmail(email: String, uid: String){
        viewModelScope.launch {
            userRepo.updateEmail(email, uid)
            _email.value = email
        }
    }
}