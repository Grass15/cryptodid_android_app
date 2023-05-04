package com.loginid.cryptodid.domain.repository


import com.loginid.cryptodid.data.local.entity.UserAndVC
import com.loginid.cryptodid.data.local.entity.UserEntity
import com.loginid.cryptodid.data.local.entity.VCEntity
import com.loginid.cryptodid.data.local.entity.VCType
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertUser(user: UserEntity)

    suspend fun insertVC(vc: VCEntity)

    suspend fun deleteVCById(claimId: String)

    fun getUserWithVCs(userId: String): Flow<UserAndVC>
    //suspend fun getUserWithClaims(userId: String): UserAndVC

    fun getUsersWithVCs(): Flow<List<UserAndVC>>

    suspend  fun checkUserCreds(username: String, pass: String): Boolean

    suspend fun getUserByUserName(username: String): UserEntity

    suspend fun getVCByTitle(userId: String,vcTitle: String): VCEntity
    fun getVCsByType(userId: String, vcType: VCType): Flow<List<VCEntity>>
}