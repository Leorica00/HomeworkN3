package com.example.homework3.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework3.domain.util.Resource
import com.example.homework3.domain.usecase.IsUserSavedUseCase
import com.example.homework3.domain.usecase.LoginUserUseCase
import com.example.homework3.domain.usecase.SaveUserUseCase
import com.example.homework3.presentation.state.LoginState
import com.example.homework3.presentation.util.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val isUserSavedUseCase: IsUserSavedUseCase,
    private val saveUserUseCase: SaveUserUseCase
): ViewModel() {
    private val _loginStateFlow = MutableStateFlow(LoginState())
    val loginStateFlow = _loginStateFlow.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LoginUIEvent>(extraBufferCapacity = 1, replay = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            if (isUserSavedUseCase()) {
                _uiEvent.emit(LoginUIEvent.GoToResultPage)
            }
        }
    }

    fun onRegisterButtonClick() {
        viewModelScope.launch {
            _uiEvent.emit(LoginUIEvent.GoToRegistrationPage)
        }
    }

    fun logInUser(username: String, password: String) {
        viewModelScope.launch {
            loginUserUseCase(username, password).collect { resource ->
                when(resource) {
                    is Resource.Loading -> _loginStateFlow.update { currentState -> currentState.copy(isLoading = resource.loading) }
                    is Resource.Success ->
                        viewModelScope.launch {
                            saveUserUseCase()
                            _uiEvent.emit(LoginUIEvent.GoToResultPage)
                        }
                    is Resource.Error -> _loginStateFlow.update { currentState -> currentState.copy(error = getErrorMessage(resource.error)) }
                }
            }
        }
    }

    sealed interface LoginUIEvent {
        data object GoToResultPage: LoginUIEvent
        data object GoToRegistrationPage: LoginUIEvent
    }
}