package com.loginid.cryptodid.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndVC(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "claimOwner"
    )
    val VCs: List<VCEntity>
){

}
