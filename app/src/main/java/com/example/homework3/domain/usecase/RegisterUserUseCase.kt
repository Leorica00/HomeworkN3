package com.example.homework3.domain.usecase

import com.example.homework3.domain.util.Resource
import com.example.homework3.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> {
        return firebaseAuthRepository.register(email, password)
    }
}