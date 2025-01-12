package com.example.homework3.presentation.util

import com.example.homework3.R
import com.example.homework3.domain.util.HandleErrorStates

fun getErrorMessage(error: HandleErrorStates): Int {
    return when (error.errorCode) {
        HandleErrorStates.ErrorCode.CLIENT_ERROR -> R.string.error_client
        HandleErrorStates.ErrorCode.SERVER_ERROR -> R.string.error_server
        HandleErrorStates.ErrorCode.HTTP_ERROR -> R.string.error_http
        HandleErrorStates.ErrorCode.NETWORK_ERROR -> R.string.no_internet
        HandleErrorStates.ErrorCode.TIMEOUT_ERROR -> R.string.error_timeout
        HandleErrorStates.ErrorCode.UNKNOWN_ERROR -> R.string.unexpected_error
        HandleErrorStates.ErrorCode.INVALID_CREDENTIALS -> R.string.wrong_creds
    }
}