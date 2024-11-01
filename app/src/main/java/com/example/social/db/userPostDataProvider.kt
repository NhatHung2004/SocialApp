package com.example.social.db

import com.example.social.R


object userPostDataProvider{
    val userPost=
        userPosts(
            avt=userAvatars[0],
            name="Loi Nguyen",
            time="1h",
            optionIcon= R.drawable.meatballsmenu,
            content="Hello World",
            postIcon = R.drawable.happiness,
            postPic = R.drawable.anh1,
            blueLikeIcon = R.drawable.like1,
            loveIcon = R.drawable.heart,
            count = 100,
            quantity = "k",
            like = R.drawable.like,
            likeText = "Thích",
            comment = R.drawable.speechbubble,
            cmtText = "Bình luận",
            isLiked=false
        )
    val userPostList=listOf(
        userPost,
        userPosts(
            avt=userAvatars[2],
            name="Hung Nguyen",
            time="2h",
            optionIcon=R.drawable.meatballsmenu,
            content="Hello World 1 ",
            postIcon = R.drawable.happiness,
            postPic = R.drawable.anh1,
            blueLikeIcon = R.drawable.like1,
            loveIcon = R.drawable.heart,
            count = 100,
            quantity = "k",
            like = R.drawable.like,
            likeText = "Thích",
            comment = R.drawable.speechbubble,
            cmtText = "Bình luận",
            isLiked=false
        ),
        userPosts(
            avt=userAvatars[3],
            name="Le Bao",
            time="5h",
            optionIcon=R.drawable.meatballsmenu,
            content="Hello World 2 ",
            postIcon = R.drawable.happiness,
            postPic = R.drawable.anh1,
            blueLikeIcon = R.drawable.like1,
            loveIcon = R.drawable.heart,
            count = 100,
            quantity = "k",
            like = R.drawable.like,
            likeText = "Thích",
            comment = R.drawable.speechbubble,
            cmtText = "Bình luận",
            isLiked=false
        ),
        userPosts(
            avt=userAvatars[4],
            name="Long Nguyen",
            time="1d",
            optionIcon=R.drawable.meatballsmenu,
            content="Hello World 1 ",
            postIcon = R.drawable.happiness,
            postPic = R.drawable.anh1,
            blueLikeIcon = R.drawable.like1,
            loveIcon = R.drawable.heart,
            count = 100,
            quantity = "k",
            like = R.drawable.like,
            likeText = "Thích",
            comment = R.drawable.speechbubble,
            cmtText = "Bình luận",
            isLiked=false
        ),
        userPosts(
            avt=userAvatars[1],
            name="Linh Nguyen",
            time="2h",
            optionIcon=R.drawable.meatballsmenu,
            content="Hello World 1 ",
            postIcon = R.drawable.happiness,
            postPic = R.drawable.anh1,
            blueLikeIcon = R.drawable.like1,
            loveIcon = R.drawable.heart,
            count = 100,
            quantity = "k",
            like = R.drawable.like,
            likeText = "Thích",
            comment = R.drawable.speechbubble,
            cmtText = "Bình luận",
            isLiked=false
        )
    )
    val cmt=
        cmtInfo(
            avtCmt=userAvatars[0],
            nameCmt="Loi Nguyen",
            timeCmt="1h",
            contentCmt="Hello World"
        )

    val cmtList=listOf(
        cmt,
        cmtInfo(
            avtCmt=userAvatars[2],
            nameCmt="Hung Nguyen",
            timeCmt="2h",
            contentCmt="Hello World 1 "
        ),
        cmtInfo(
            avtCmt=userAvatars[3],
            nameCmt="Le Bao",
            timeCmt="5h",
            contentCmt="Hello World 2 "
        ),
        cmtInfo(
            avtCmt=userAvatars[4],
            nameCmt="Long Nguyen",
            timeCmt="1d",
            contentCmt="Hello World 1 "
        ),
        cmtInfo(
            avtCmt=userAvatars[1],
            nameCmt="Linh Nguyen",
            timeCmt="2h",
            contentCmt="Hello World 1 "
        ),
        cmtInfo(
            avtCmt=userAvatars[1],
            nameCmt="Linh Nguyen",
            timeCmt="2h",
            contentCmt="Hello World 1 "
        ),
        cmtInfo(
            avtCmt=userAvatars[1],
            nameCmt="Linh Nguyen",
            timeCmt="2h",
            contentCmt="Hello World 1 "
        ),  cmtInfo(
            avtCmt=userAvatars[1],
            nameCmt="Linh Nguyen",
            timeCmt="2h",
            contentCmt="Hello World 1 "
        ),  cmtInfo(
            avtCmt=userAvatars[1],
            nameCmt="Linh Nguyen",
            timeCmt="2h",
            contentCmt="Hello World 1 "
        ),
        cmtInfo(
            avtCmt=userAvatars[4],
            nameCmt="Long Nguyen",
            timeCmt="1d",
            contentCmt="Hello World 1 "
        ),
    )
    val nofication=
        noficationInfo(
            avtNof =userAvatars[0],
            nameNof = "Loi Nguyen",
            timeNoff="1 năm",
            contentNof="Da gui loi moi ket ban"
        )
    val nofList=listOf(
        nofication,
        noficationInfo(
            avtNof =userAvatars[3],
            nameNof = "Loi Nguyen",
            timeNoff="1 tháng",
            contentNof="Da dong y ket ban",
        ),
        noficationInfo(
            avtNof =userAvatars[1],
            nameNof = "Hung Nguyen",
            timeNoff="1 giờ ",
            contentNof="Da thich bai viet cua ban",
        ),
        noficationInfo(
            avtNof =userAvatars[2],
            nameNof = "Le Bao",
            timeNoff="1 giờ ",
            contentNof="Da binh luan bai viet cua ban",
        ),
        noficationInfo(
            avtNof =userAvatars[4],
            nameNof = "Linh Nguyen",
            timeNoff="1 ngày",
            contentNof="Da dang 1 bai viet gan day",
        ),
    )
    val friend=
        friendData(
            avtFriend =userAvatars[0],
            nameFriend="Loi Nguyen",
            timeFriend="1h",
        )
    val friendList=listOf(
        friend,
        friendData(
            avtFriend=userAvatars[2],
            nameFriend="Hung Nguyen",
            timeFriend="2h",
        ),
        friendData(
            avtFriend=userAvatars[3],
            nameFriend="Bao Nguyen",
            timeFriend="2h",
        ),
        friendData(
            avtFriend=userAvatars[4],
            nameFriend="Linh Nguyen",
            timeFriend="2h",
        ),
        friendData(
            avtFriend=userAvatars[1],
            nameFriend="Long Nguyen",
            timeFriend="2h",
        )
    )
}


