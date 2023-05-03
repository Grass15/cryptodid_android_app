package com.loginid.cryptodid.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.loginid.cryptodid.utils.UserDataPrefrence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

/**
 * This datastore repository helps us to manipulates the datastore feature in android.
 * we can a pair of key-value is this case we use it to handle a boarding screen, (a screen that can be shown only one time)
 */
class DataStoreRepository(context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
    }

    private val dataStore = context.dataStore

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }
}


class UserDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val userNameKey = stringPreferencesKey(name = "userName")
        val userIdKey = stringPreferencesKey(name = "userId")
        val nameKey = stringPreferencesKey(name = "name")
        val tokenKey = stringPreferencesKey(name = "token")
    }

    private val dataStore = context.dataStore

    suspend fun saveUserDataState(userPref: UserDataPrefrence) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userNameKey] = userPref.userName
            preferences[PreferencesKey.userIdKey] = userPref.userId
            preferences[PreferencesKey.nameKey] = userPref.name
          //  preferences[PreferencesKey.tokenKey] = userPref.token!!
        }
    }

    fun readUserDataState(): Flow<UserDataPrefrence> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val userDataState = UserDataPrefrence(
                    userName =  preferences[PreferencesKey.userNameKey].toString(),
                    userId =  preferences[PreferencesKey.userIdKey].toString(),
                    name =  preferences[PreferencesKey.nameKey].toString(),
                )
                    userDataState
            }
    }
}
