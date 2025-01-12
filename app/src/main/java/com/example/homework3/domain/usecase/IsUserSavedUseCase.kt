package com.example.homework3.domain.usecase

import com.example.homework3.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class IsUserSavedUseCase@Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(): Boolean{
        return firebaseAuthRepository.isUserLoggedIn()
    }
}