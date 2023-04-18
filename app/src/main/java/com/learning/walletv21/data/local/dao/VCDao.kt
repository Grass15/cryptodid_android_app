package com.learning.walletv21.data.local.dao

import androidx.room.*
import com.learning.walletv21.data.local.entity.UserAndVC
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.data.local.entity.VCEntity
import com.learning.walletv21.utils.Constants.USER_TABLE_NAME
import com.learning.walletv21.utils.Constants.VC_TABLE_NAME
import kotlinx.coroutines.flow.Flow


/**
 * Defining all the needed methods to interact with the local cashing system (room dataBase),
 * Note that all the methods should be executed inside a kotlin couroutine so we don't block our
 * main thread, we do that by using suspend keyword or returning an object of type Flow
 */
@Dao
interface VCDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(vc: VCEntity)

    @Query("DELETE FROM $VC_TABLE_NAME WHERE id = :claimId")
    suspend fun deleteClaimById(claimId: Int)

    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE userId = :userId")
   // suspend fun getUserWithClaims(userId: String): UserAndVC
    fun getUserWithClaims(userId: String): Flow<UserAndVC>

    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME")
    fun getUsersWithClaims(): Flow<List<UserAndVC>>

    @Query("SELECT EXISTS(SELECT 1 FROM $USER_TABLE_NAME WHERE username = :username AND password = :pass)")
    suspend fun checkUserCreds(username: String, pass: String): Boolean

}