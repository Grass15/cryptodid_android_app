package com.loginid.cryptodid.di

import android.content.Context
import androidx.room.Room
import com.loginid.cryptodid.data.local.VCDataBase
import com.loginid.cryptodid.data.local.dao.VCDao
import com.loginid.cryptodid.data.repository.UserRepositoryImpl
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.utils.Constants.VC_DATA_BASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Database module provides all needed dependencies in our project in optimal way
 * using dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    /**
     * provides a Singleton database instance
     */
    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ):VCDataBase{
       // val dbFile = File(context.applicationContext.filesDir, "vcdatabase.db")
       // dbFile.delete()
        return Room.databaseBuilder(
            context,
            VCDataBase::class.java,
            VC_DATA_BASE
        )/*.fallbackToDestructiveMigration().*/.build()
    }

    @Provides
    fun provideVCDao(database: VCDataBase): VCDao {
        return database.vcDao()
    }

    /**
     * provides the UserRepository to interact with the database
     * Note : here we can return a fake repository to test functionalities
     */
    @Provides
    fun provideUserRepository(vcDao: VCDao): UserRepository {
        return UserRepositoryImpl(vcDao)
    }
}