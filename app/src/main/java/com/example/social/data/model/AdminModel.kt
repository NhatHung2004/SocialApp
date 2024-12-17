package com.example.social.data.model

data class Admin(
    val id: Int,
    var username: String,
    var password: String,
    val avatar: Int = 0
)
object DataProviderAdmin {
    val admin = mutableListOf(
        Admin(1,"admin1@gmail.com", "111111"),
        Admin(2,"admin2@gmail.com", "222222"),
        Admin(3,"admin3@gmail.com", "333333"),
    )
}