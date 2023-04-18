package com.learning.walletv21.presentation.authentication.register

import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.presentation.authentication.login.Status


data class RegisterDataState(
    val username: String = "",
    val password: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val phone: String = "",
    val address: String = "",
    val userId: String = "",
    val repassword: String = "",
    val status: Status = Status.NO_ACTION
){
    fun toUserEntity(): UserEntity{
        return UserEntity(
            userId = userId,
            username = username,
            password = password,
            firstname = firstname,
            lastname = lastname,
            phone = phone,
            address = address
        )
    }
}