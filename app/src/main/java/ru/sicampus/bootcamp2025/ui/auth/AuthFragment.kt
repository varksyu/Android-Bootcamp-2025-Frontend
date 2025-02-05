package ru.sicampus.bootcamp2025.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
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

        viewBinding.button.setOnClickListener {
            viewModel.auth(viewBinding.userLogin.toString(), viewBinding.userPassword.toString())
        }

        viewModel.state.collectWithLifecycle(this) { state ->
            if (state is AuthViewModel.State.Show) {
                viewBinding.errorText.visibility =
                    if (state.errorText == null)  View.GONE else View.VISIBLE
            }

        }
        viewBinding.userLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(p0: Editable?) {
                viewModel.changeLogin()
            }

        })
    }


    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}