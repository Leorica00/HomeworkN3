package com.example.homework3.domain.repository

import com.example.homework3.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun register(email: String, password: String): Flow<Resource<Boolean>>
    suspend fun logIn(email: String, password: String): Flow<Resource<Boolean>>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun saveLoggedInUser()
}