package ru.sicampus.bootcamp2025.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.R
import ru.sicampus.bootcamp2025.data.center.CenterNetworkDataSource
import ru.sicampus.bootcamp2025.data.center.CenterRepoImpl
import ru.sicampus.bootcamp2025.domain.auth.IsUserExistUseCase
import ru.sicampus.bootcamp2025.domain.auth.LoginUseCase
import ru.sicampus.bootcamp2025.domain.center.GetCentersUseCase
import ru.sicampus.bootcamp2025.ui.centerList.CenterListViewModel

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

        }
    }

    fun changeLogin() {
        isNewUser = null
        viewModelScope.launch {
            updateState()
        }
    }

    private suspend fun updateState() {
        _state.emit(getStateShow())
    }

    private fun getStateShow() : State {
        return State.Show(
            errorText = when (isNewUser) {
                false -> null
                true -> getApplication<Application>().getString(R.string.error_user_not_exist)
                null -> null
            }
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


        }
    }
}