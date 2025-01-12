package com.example.homework3.presentation.screen.login

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.homework3.R
import com.example.homework3.databinding.FragmentLogInBinding
import com.example.homework3.presentation.base.BaseFragment
import com.example.homework3.presentation.state.LoginState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun setUp() {}

    override fun setUpListeners() {
        binding.loginBtn.setOnClickListener {
            viewModel.logInUser(
                username = binding.loginEmailEt.text.toString(),
                password = binding.loginPasswordEt.text.toString()
            )
        }

        binding.btnGoToSignUp.setOnClickListener {
            viewModel.onRegisterButtonClick()
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStateFlow.collect {
                    handleState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { uiState ->
                    when (uiState) {
                        LoginViewModel.LoginUIEvent.GoToResultPage -> {
                            requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                                LogInFragmentDirections
                                    .actionLogInFragmentToResultFragment()
                            )
                        }

                        LoginViewModel.LoginUIEvent.GoToRegistrationPage -> {
                            requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                                LogInFragmentDirections
                                    .actionLogInFragmentToRegistrationFragment()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleState(placesState: LoginState) {
        placesState.error?.let {
            Toast.makeText(requireContext(), requireContext().getString(it), Toast.LENGTH_SHORT)
                .show()
        }
    }
}