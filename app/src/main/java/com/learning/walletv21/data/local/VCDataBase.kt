package com.learning.walletv21.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.learning.walletv21.data.local.dao.VCDao
import com.learning.walletv21.data.local.entity.Claim_TypeConverter
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.data.local.entity.VCEntity


@Database(entities = [VCEntity::class, UserEntity::class],
    version = 4
)
@TypeConverters(Claim_TypeConverter::class)
abstract class VCDataBase: RoomDatabase() {
    abstract fun vcDao(): VCDao
}