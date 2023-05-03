package com.loginid.cryptodid.data.repository

import com.loginid.cryptodid.data.local.dao.VCDao
import com.loginid.cryptodid.data.local.entity.UserAndVC
import com.loginid.cryptodid.data.local.entity.UserEntity
import com.loginid.cryptodid.data.local.entity.VCEntity
import com.loginid.cryptodid.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Using repository pattern to to abstract the data sources from the
 * rest of the application. In Android, the Repository pattern is often
 * used in combination with the Model-View-ViewModel (MVVM) architecture
 * to separate concerns and make the code more maintainable and testable.
 */
class UserRepositoryImpl @Inject constructor(
      private val vcDao: VCDao
): UserRepository {

    /**
     * Inserting a new user to the database (will be synchronized with a cloud service later)
     * @param user
     */
    override suspend fun insertUser(user: UserEntity) {
        return vcDao.insertUser(user)
    }

    /**
     * Appending a new vc to the user's vc list
     * @param vc
     */

    override suspend fun insertVC(vc: VCEntity) {
        return vcDao.insertVC(vc)
    }

    /**
     * Deleting a vc
     * @param vcID
     */

    override suspend fun deleteVCById(claimId: String) {
         vcDao.deleteVCById(claimId)
    }

    /**
     * Getting VCs that belongs to the user with id userId
     * @param userId
     * @return Flow<UserAndVC> which contains the VC list
     */
    override  fun getUserWithVCs(userId: String): Flow<UserAndVC>{
        return vcDao.getUserWithVCs(userId)
    }

    override fun getUsersWithVCs(): Flow<List<UserAndVC>> {
        TODO("Not yet implemented")
    }

    /**
     * Checking the user credentials before redirecting to home screen
     * @param username
     * @param pass password
     */
    override suspend fun checkUserCreds(username: String, pass: String): Boolean {
        return  vcDao.checkUserCreds(username,pass)
    }

    override suspend fun getUserByUserName(username: String): UserEntity {
        return  vcDao.getUserByUserName(username)
    }
}