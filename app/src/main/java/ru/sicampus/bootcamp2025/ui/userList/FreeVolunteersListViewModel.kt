package ru.sicampus.bootcamp2025.ui.userList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource
import ru.sicampus.bootcamp2025.data.user.UserNetworkDataSource
import ru.sicampus.bootcamp2025.data.user.UserRepoImpl
import ru.sicampus.bootcamp2025.domain.user.GetUserUseCase
import ru.sicampus.bootcamp2025.domain.user.UserEntity
import ru.sicampus.bootcamp2025.domain.user.GetUserListUseCase
import androidx.lifecycle.ViewModelProvider

class FreeVolunteersListViewModel(
    private val getUserListUseCase: GetUserListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        updateState()
    }

    fun clickRefresh() {
        updateState()
    }
    private fun updateState() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            _state.emit(
                getUserListUseCase.invoke().fold(
                    onSuccess = { data ->
                        Log.d("uraa", "успех успех ${data.toString()}")
                        ru.sicampus.bootcamp2025.ui.userList.FreeVolunteersListViewModel.State.Show(data)
                    },
                    onFailure = { error ->
                        Log.d("kaput", error.message.toString())
                        ru.sicampus.bootcamp2025.ui.userList.FreeVolunteersListViewModel.State.Error(error.message.toString())
                    }
                )
            )
        }
    }


    sealed interface State {
        data object Loading: State
        data class Show(
            val items: List<UserEntity>
        ) : State
        data class Error(
            val text: String
        ) : State
    }
    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FreeVolunteersListViewModel(
                    getUserListUseCase = GetUserListUseCase(
                        repo = UserRepoImpl(
                            userNetworkDataSource = UserNetworkDataSource()
                        ),
                    )
                ) as T
            }
        }
    }

}
