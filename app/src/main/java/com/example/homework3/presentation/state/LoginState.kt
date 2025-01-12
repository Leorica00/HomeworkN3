package com.example.homework3.presentation.state

import androidx.annotation.StringRes

data class LoginState(
    @StringRes val error: Int? = null,
    val isLoading: Boolean = false
)