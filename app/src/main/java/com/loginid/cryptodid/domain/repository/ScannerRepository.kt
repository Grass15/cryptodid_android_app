package com.loginid.cryptodid.domain.repository

import kotlinx.coroutines.flow.Flow


interface ScannerRepository {
    fun startScanning(): Flow<String?>
}