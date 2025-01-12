package com.example.homework3.data.repository

import com.example.homework3.domain.util.Resource
import com.example.homework3.data.remote.common.EmailSignInResponseHandler
import com.example.homework3.domain.repository.FirebaseAuthRepository
import com.example.homework3.domain.repository.SharedPrefsRepository
import com.example.homework3.domain.util.HandleErrorStates
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okio.IOException

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val emailSignInResponseHandler: EmailSignInResponseHandler,
    private val sharedPrefsRepository: SharedPrefsRepository
) : FirebaseAuthRepository {
    override suspend fun register(email: String, password: String): Flow<Resource<Boolean>> {
        return flow {
            var success: Boolean = false
            var exception: Exception? = null
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        success = true
                    }else {
                        success = false
                        exception = task.exception
                    }
                }.await()

                delay(2000)

                if (success) emit(Resource.Success(true)) else emit(Resource.Error(HandleErrorStates.handleException(exception ?: Exception()), throwable = exception?: Exception()))
            } catch (e: Exception) {
                emit(Resource.Error(HandleErrorStates.handleException(e), throwable = e))
            }
        }

    }

    override suspend fun logIn(email: String, password: String): Flow<Resource<Boolean>> {
        return emailSignInResponseHandler.safeAuthCall {
            auth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return sharedPrefsRepository.readBoolean(IS_USER_LOGGED_IN, false)
    }

    override suspend fun saveLoggedInUser() {
        sharedPrefsRepository.putBoolean(IS_USER_LOGGED_IN, true)
    }

    companion object {
        private const val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"
    }
}