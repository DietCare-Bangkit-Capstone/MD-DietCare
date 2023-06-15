package com.capstone.dietcare.data.local.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun login(login: LoginModel) {
        dataStore.edit { preferences ->
            preferences[SESSION_KEY] = true
            preferences[EMAIL_KEY] = login.email
            preferences[PASSWORD_KEY] = login.password
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[SESSION_KEY] = false
            preferences[EMAIL_KEY] = ""
            preferences[PASSWORD_KEY] = ""
        }
    }

    fun isLogin(): Flow<LoginModel> {
        return dataStore.data.map { preferences ->
            LoginModel(
                preferences[SESSION_KEY] ?: false,
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: ""
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreference? = null

        private val SESSION_KEY = booleanPreferencesKey("isLogin")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}