package com.loginid.cryptodid.data.local.dao

import androidx.room.*
import com.loginid.cryptodid.data.local.entity.UserAndVC
import com.loginid.cryptodid.data.local.entity.UserEntity
import com.loginid.cryptodid.data.local.entity.VCEntity
import com.loginid.cryptodid.utils.Constants.USER_TABLE_NAME
import com.loginid.cryptodid.utils.Constants.VC_TABLE_NAME
import kotlinx.coroutines.flow.Flow


/**
 * Defining all the needed methods to interact with the local cashing system (room dataBase),
 * Note that all the methods should be executed inside a kotlin couroutine so we don't block our
 * main thread, we do that by using suspend keyword or returning an object of type Flow,
 * room database is one of the most popular ways to store objects in the local cash of the phone, it
 * it is implemented barely the same way as an API interface IssuerAPI in our case, we use interfaces instead
 * of classes or abstract classes, thank to the power of annotations (@GET, @Insert ...)
 */
@Dao
interface VCDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    /**
     * Adding a vc in room database is an easy task we just pass an object of
     * type VCEntity and @Instert annotation do all the job
     * @param vc
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVC(vc: VCEntity)

    /**
     * deleting a VC can be done in 2 ways :
     * either we pass the id of the vc and then we execute a delete query using @Query annotation,
     * or we can just pass the vc object and we use @Delete annotation, both approaches are valid in our case
     * @param vcId
     */
    @Query("DELETE FROM $VC_TABLE_NAME WHERE id = :vcId")
    suspend fun deleteVCById(vcId: String)

    /**
     * In order to display a user VC in our homescreen we use a flow of data so whenever we delete a vc or
     * append a vc to the user's vc list it is automatically updated
     * @param userId
     * @return Flow<UserAndVC>
     */
    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE userId = :userId")
   // suspend fun getUserWithClaims(userId: String): UserAndVC
    fun getUserWithVCs(userId: String): Flow<UserAndVC>

    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME")
    fun getUsersWithVCs(): Flow<List<UserAndVC>>

    /**
     * checking user credentials (login)
     * @param username
     * @param pass
     * @return Boolean
     */
    @Query("SELECT EXISTS(SELECT 1 FROM $USER_TABLE_NAME WHERE username = :username AND password = :pass)")
    suspend fun checkUserCreds(username: String, pass: String): Boolean

    /**
     * getting user data to store them inside the userDataStore prefrence
     * @param username
     * @return UserEntity
     */
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE username = :username")
    suspend fun getUserByUserName(username: String): UserEntity
}