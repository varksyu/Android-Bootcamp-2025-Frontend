package ru.sicampus.bootcamp2025.ui.entry.register

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.R
import ru.sicampus.bootcamp2025.data.auth.AuthNetworkDataSource
import ru.sicampus.bootcamp2025.data.auth.AuthRepoImpl
import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource
import ru.sicampus.bootcamp2025.domain.auth.IsUserExistUseCase
import ru.sicampus.bootcamp2025.domain.auth.RegisterUserUseCase
import kotlin.reflect.KClass

class RegisterViewModel(
    private val application: Application,
    private val isUserExistUseCase: IsUserExistUseCase,
    private  val registerUseCase: RegisterUserUseCase
) : AndroidViewModel(application = application) {
    private val _state = MutableStateFlow<State>(getStateShow())
    val state = _state.asStateFlow()

    private val _navigateToMain = MutableStateFlow(false)
    val navigateToMain = _navigateToMain.asStateFlow()

    private var isNewUser : Boolean? = null

    init {
        viewModelScope.launch {
            updateState()
        }

    }
    fun register(
        login : String,
        password : String,
        name : String
    )
    {
        viewModelScope.launch {
            _state.emit(State.Loading)
            when (isNewUser) {
                false -> {
                    updateState(getApplication<Application>().getString(R.string.error_user_exist))
                }
                true -> {
                    registerUseCase(login, password, name).fold(
                        onSuccess = {
                            TODO() // Сделать переход на MainActivity
                        },
                        onFailure =  { error ->
                            updateState(error.toString())
                        }
                    )
                }
                null -> {
                    isUserExistUseCase(login).fold(
                        onSuccess = { isExist ->
                            isNewUser = isExist
                            updateState()
                        },
                        onFailure =  { error ->
                            updateState(error.toString())
                        }
                    )
                }
            }

        }
    }



    fun changeLogin() {
        isNewUser = null
        viewModelScope.launch {
            updateState()
        }
    }

    private suspend fun updateState(error : String? = null) {
        _state.emit(getStateShow(error))
    }

    private fun getStateShow(error : String? = null) : State {
        return State.Show(
            errorText = error
        )
    }


    sealed interface State {
        data object Loading : State
        data class Show(
            var errorText : String?
        ) : State
    }


    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                val authRepoImpl = AuthRepoImpl(
                    authNetworkDataSource = AuthNetworkDataSource,
                    authStorageDataSource = AuthStorageDataSource
                )
                return RegisterViewModel(
                    application = application,
                    isUserExistUseCase = IsUserExistUseCase(authRepoImpl),
                    registerUseCase = RegisterUserUseCase(authRepoImpl)
                ) as T
            }

        }
    }
}