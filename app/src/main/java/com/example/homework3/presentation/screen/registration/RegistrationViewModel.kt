package com.example.homework3.presentation.screen.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework3.domain.usecase.RegisterUserUseCase
import com.example.homework3.domain.usecase.SaveUserUseCase
import com.example.homework3.domain.util.Resource
import com.example.homework3.presentation.state.LoginState
import com.example.homework3.presentation.state.RegistrationState
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
class RegistrationViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    private val _registrationStateFlow = MutableStateFlow(RegistrationState())
    val registrationStateFlow = _registrationStateFlow.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegistrationUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            registerUserUseCase(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _registrationStateFlow.update { currentState ->
                        currentState.copy(
                            isLoading = resource.loading
                        )
                    }

                    is Resource.Success ->
                        viewModelScope.launch {
                            saveUserUseCase()
                            _uiEvent.emit(RegistrationUIEvent.GoToResultPage)
                        }

                    is Resource.Error -> _registrationStateFlow.update { currentState ->
                        currentState.copy(
                            error = getErrorMessage(resource.error)
                        )
                    }
                }
            }
        }
    }

    sealed interface RegistrationUIEvent {
        data object GoToResultPage : RegistrationUIEvent
    }
}