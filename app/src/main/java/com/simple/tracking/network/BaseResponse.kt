package com.simple.tracking.network

import com.simple.tracking.model.User

data class BaseResponse (
    val success: Boolean,
    val messages: String,
    val data: List<User> // Ini bisa berupa List<User> atau objek lain
)