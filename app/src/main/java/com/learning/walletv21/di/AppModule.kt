package com.learning.walletv21.di

import com.learning.walletv21.data.local.dao.VCDao
import com.learning.walletv21.data.remote.IssuerApi
import com.learning.walletv21.data.repository.ClaimRepositoryImpl
import com.learning.walletv21.data.repository.VCRepositoryImp
import com.learning.walletv21.domain.repository.ClaimRepository
import com.learning.walletv21.domain.repository.VCRepository
import com.learning.walletv21.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * The APP Module provide our application with all the common dependencies
 * we use through a dependency injection process
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * Setup our retrofit builder so we can make the api call
     */
    @Provides
    @Singleton
    fun provideIssuerApi(): IssuerApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssuerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClaimRepository(api: IssuerApi): ClaimRepository {
        return ClaimRepositoryImpl(api)
    }

    /**
     * Provides the actual implementation of the vc repository we can return here
     * a fake implementation to test our views and api,
     * @param api
     * @param dao
     * @return VCRepositoryImp(api,dao)
     */

    @Provides
    @Singleton
    fun provideVCRepository(api: IssuerApi,dao: VCDao): VCRepository {
        return VCRepositoryImp(api,dao)
    }

}