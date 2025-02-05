package ru.sicampus.bootcamp2025.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.sicampus.bootcamp2025.R
import ru.sicampus.bootcamp2025.databinding.FragmentAuthBinding
import ru.sicampus.bootcamp2025.utils.collectWithLifecycle

class AuthFragment : Fragment(R.layout.fragment_auth) {
    private var _viewBinding : FragmentAuthBinding? = null
    private val viewBinding : FragmentAuthBinding get() = _viewBinding!!

    private val viewModel by viewModels<AuthViewModel> { AuthViewModel.Factory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = FragmentAuthBinding.bind(view)

        viewModel.state.collectWithLifecycle(this) { state ->


        }

    }


    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}