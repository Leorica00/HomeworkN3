package com.example.homework3.domain.repository

interface SharedPrefsRepository {
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun readBoolean(key: String, defaultValue: Boolean): Boolean
}