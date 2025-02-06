package ru.sicampus.bootcamp2025.ui.entry.auth

import android.app.Application
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
import ru.sicampus.bootcamp2025.data.center.CenterNetworkDataSource
import ru.sicampus.bootcamp2025.data.center.CenterRepoImpl
import ru.sicampus.bootcamp2025.domain.auth.IsUserExistUseCase
import ru.sicampus.bootcamp2025.domain.auth.LoginUseCase
import ru.sicampus.bootcamp2025.domain.center.GetCentersUseCase
import ru.sicampus.bootcamp2025.ui.centerList.CenterListViewModel
import kotlin.reflect.KClass

class AuthViewModel(
    private val application: Application,
    private val isUserExistUseCase: IsUserExistUseCase,
    private  val loginUseCase: LoginUseCase
) : AndroidViewModel(application = application) {
    private val _state = MutableStateFlow<State>(getStateShow())
    val state = _state.asStateFlow()

    private var isNewUser : Boolean? = null


    init {
        viewModelScope.launch {
            updateState()
        }

    }
    fun auth(
        login : String,
        password : String
    )
    {
        viewModelScope.launch {
            _state.emit(State.Loading)
            when (isNewUser) {
                true -> {
                    updateState(getApplication<Application>().getString(R.string.error_user_not_exist))
                }
                false -> {
                    loginUseCase(login, password).fold(
                        onSuccess = {
                            TODO()
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
                return AuthViewModel(
                    application = application,
                    isUserExistUseCase = IsUserExistUseCase(authRepoImpl),
                    loginUseCase = LoginUseCase(authRepoImpl)
                    ) as T
            }

        }
    }
}