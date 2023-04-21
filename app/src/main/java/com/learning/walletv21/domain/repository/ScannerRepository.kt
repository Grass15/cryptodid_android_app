package com.learning.walletv21.domain.repository

import kotlinx.coroutines.flow.Flow

interface ScannerRepository {
    fun startScanning(): Flow<String?>
}