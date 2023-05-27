package com.loginid.cryptodid.di


import com.loginid.cryptodid.data.repository.ScannerRepositoryImp
import com.loginid.cryptodid.domain.repository.ScannerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Provides the QR Scanner feature in our application
 */
@InstallIn(ViewModelComponent::class)
@Module
abstract class ScannerRepositoryModule {


    @Binds
    @ViewModelScoped
    abstract fun bindScannerRepository(
        scannerRepositoryImp : ScannerRepositoryImp
    ) : ScannerRepository
}