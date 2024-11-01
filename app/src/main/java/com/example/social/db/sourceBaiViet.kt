package com.example.social.db

data class userData(
    val avt:UserAvatar,
    val name:String,
    val coverPhoto:CoverPhoto,
    val status:Int,
    val friend:Int,
    val follow:Int
)

data class userPosts(
    val avt:UserAvatar,
    val name:String,
    val time:String,
    val optionIcon:Int,
    val content:String,
    val postIcon:Int,
    val postPic:Int,
    val blueLikeIcon:Int,
    val loveIcon:Int,
    var count:Int,
    val quantity:String,
    val like:Int,
    val likeText:String,
    val comment:Int,
    val cmtText:String,
    val isLiked:Boolean
)

data class cmtInfo(
    val avtCmt:UserAvatar,
    val nameCmt:String,
    val timeCmt:String,
    val contentCmt:String
)
data class noficationInfo(
    val avtNof:UserAvatar,
    val nameNof:String,
    val timeNoff:String,
    val contentNof:String,
)
data class friendData(
    val avtFriend:UserAvatar,
    val nameFriend:String,
    val timeFriend:String,
)