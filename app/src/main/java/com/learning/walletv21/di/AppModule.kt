package com.learning.walletv21.di

import com.learning.walletv21.data.remote.IssuerApi
import com.learning.walletv21.data.repository.ClaimRepositoryImpl
import com.learning.walletv21.domain.repository.ClaimRepository
import com.learning.walletv21.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
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


}