package com.learning.walletv21.domain.repository


import com.learning.walletv21.data.local.entity.UserAndVC
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.data.local.entity.VCEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertUser(user: UserEntity)

    suspend fun insertVC(vc: VCEntity)

    suspend fun deleteVCById(claimId: String)

    fun getUserWithVCs(userId: String): Flow<UserAndVC>
    //suspend fun getUserWithClaims(userId: String): UserAndVC

    fun getUsersWithVCs(): Flow<List<UserAndVC>>

    suspend  fun checkUserCreds(username: String, pass: String): Boolean
}