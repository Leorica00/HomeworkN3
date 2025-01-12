package com.example.homework3.presentation.screen.registration

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.homework3.R
import com.example.homework3.databinding.FragmentRegistrationBinding
import com.example.homework3.presentation.base.BaseFragment
import com.example.homework3.presentation.state.RegistrationState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val viewModel by viewModels<RegistrationViewModel>()

    override fun setUp() {}

    override fun setUpListeners() {
        binding.registerNextBtn.setOnClickListener {
            viewModel.register(
                email = binding.registerEmailEt.text.toString(),
                password = binding.registerPasswordEt.text.toString()
            )
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registrationStateFlow.collect {
                    handleState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { uiState ->
                    when (uiState) {
                        RegistrationViewModel.RegistrationUIEvent.GoToResultPage -> {
                            requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                                RegistrationFragmentDirections.actionRegistrationFragmentToResultFragment()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleState(placesState: RegistrationState) {
        placesState.error?.let {
            Toast.makeText(requireContext(), requireContext().getString(it), Toast.LENGTH_SHORT)
                .show()
        }
    }
}