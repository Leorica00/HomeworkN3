package com.example.homework3.data.remote.common

import com.example.homework3.domain.util.Resource
import com.example.homework3.domain.util.HandleErrorStates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EmailSignInResponseHandler @Inject constructor(private val ioDispatcher: CoroutineDispatcher) {
    suspend fun <T> safeAuthCall(call: suspend () -> T): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))

        try {
            call()
            emit(Resource.Success(true))
        } catch (t: Exception) {
            emit(Resource.Error(error = HandleErrorStates.handleException(t), throwable = t))
        }

        emit(Resource.Loading(false))
    }.flowOn(ioDispatcher)
}