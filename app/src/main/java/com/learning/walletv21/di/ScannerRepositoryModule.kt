package com.learning.walletv21.di


import com.learning.walletv21.data.repository.ScannerRepositoryImp
import com.learning.walletv21.domain.repository.ScannerRepository
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