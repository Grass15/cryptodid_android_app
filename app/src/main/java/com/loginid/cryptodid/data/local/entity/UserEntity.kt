package com.loginid.cryptodid.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.loginid.cryptodid.utils.Constants.USER_TABLE_NAME
import javax.annotation.Nonnull


@Entity(tableName = USER_TABLE_NAME, indices = [Index(value = ["username"], unique = true)])
data class UserEntity(
    @Nonnull
    @ColumnInfo(name = "username")
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
