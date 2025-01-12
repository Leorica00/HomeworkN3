package com.example.homework3.domain.usecase

import com.example.homework3.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke() {
        return firebaseAuthRepository.saveLoggedInUser()
    }
}