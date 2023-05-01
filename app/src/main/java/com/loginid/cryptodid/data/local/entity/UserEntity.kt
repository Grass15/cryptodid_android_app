package com.loginid.cryptodid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loginid.cryptodid.utils.Constants.USER_TABLE_NAME
import javax.annotation.Nonnull


@Entity(tableName = USER_TABLE_NAME)
data class UserEntity(
    @Nonnull
    val username: String,
    val password: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val address: String? = null,
    @PrimaryKey
    @Nonnull
    val userId: String,
){

}
