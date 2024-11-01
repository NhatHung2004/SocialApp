package com.example.social.db

import com.example.social.R

data class UserAvatar(
    val id: Int,      // ID của user (hoặc có thể là tên)
    val avatarRes: Int // Tài nguyên hình ảnh (R.drawable.avatar_x)
)

val userAvatars = listOf(
    UserAvatar(id = 1, avatarRes = R.drawable.avt1),
    UserAvatar(id = 2, avatarRes = R.drawable.avt2),
    UserAvatar(id = 3, avatarRes = R.drawable.avt3),
    UserAvatar(id = 4, avatarRes = R.drawable.avt4),
    UserAvatar(id = 5, avatarRes = R.drawable.avt5),
    UserAvatar(id = 6, avatarRes = R.drawable.avt1),
)

data class Icon( // Đổi tên lớp từ icon thành Icon
    val id: Int,      // ID của icon
    val iconRes: Int // Tài nguyên hình ảnh (R.drawable.icon_x)
)

val icons = listOf(
    Icon(id = 1, iconRes = R.drawable.heart), // Sửa thành Icon
)

data class CoverPhoto(
    val id: Int,      // ID của user (hoặc có thể là tên)
    val avatarRes: Int // Tài nguyên hình ảnh (R.drawable.avatar_x)
)

val coverPhotos = listOf(
    UserAvatar(id = 1, avatarRes = R.drawable.anhbia1),
    UserAvatar(id = 2, avatarRes = R.drawable.anhbia2),
    UserAvatar(id = 3, avatarRes = R.drawable.anhbia6),
    UserAvatar(id = 4, avatarRes = R.drawable.anhbia7),
    UserAvatar(id = 5, avatarRes = R.drawable.hinhbia),
)