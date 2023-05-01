package com.loginid.cryptodid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loginid.cryptodid.data.local.dao.VCDao
import com.loginid.cryptodid.data.local.entity.Claim_TypeConverter
import com.loginid.cryptodid.data.local.entity.UserEntity
import com.loginid.cryptodid.data.local.entity.VCEntity


@Database(entities = [VCEntity::class, UserEntity::class],
    version = 4
)
@TypeConverters(Claim_TypeConverter::class)
abstract class VCDataBase: RoomDatabase() {
    abstract fun vcDao(): VCDao
}