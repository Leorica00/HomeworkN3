package com.example.homework3.data.repository

import android.content.SharedPreferences
import com.example.homework3.domain.repository.SharedPrefsRepository

class SharedPrefsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPrefsRepository {
    override suspend fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override suspend fun readBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}