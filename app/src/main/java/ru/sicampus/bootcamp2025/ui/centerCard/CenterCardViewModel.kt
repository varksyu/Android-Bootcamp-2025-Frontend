package ru.sicampus.bootcamp2025.ui.centerCard

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource
import ru.sicampus.bootcamp2025.data.center.CenterNetworkDataSource
import ru.sicampus.bootcamp2025.data.center.CenterRepoImpl
import ru.sicampus.bootcamp2025.data.user.UserNetworkDataSource
import ru.sicampus.bootcamp2025.data.user.UserRepoImpl
import ru.sicampus.bootcamp2025.domain.center.CenterEntity
import ru.sicampus.bootcamp2025.domain.center.GetCenterUseCase
import ru.sicampus.bootcamp2025.domain.center.JoinCenterUseCase
import ru.sicampus.bootcamp2025.domain.user.GetUserUseCase
import ru.sicampus.bootcamp2025.domain.user.UserEntity
import ru.sicampus.bootcamp2025.domain.user.UserRepo
import ru.sicampus.bootcamp2025.ui.profile.ProfileFragment
import ru.sicampus.bootcamp2025.ui.profile.ProfileViewModel

class CenterCardViewModel(
    private val getCenterUseCase : GetCenterUseCase,
    private val joinCenterUseCase : JoinCenterUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel(){
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _toastMessage = MutableStateFlow<Boolean>(false)
    val toastMessage = _toastMessage.asStateFlow()

    private val _updateProfile = MutableStateFlow<Boolean>(false)
    val updateProfile = _updateProfile.asStateFlow()

    private val _volunteers = MutableLiveData<List<UserEntity>>()
    val volunteers: LiveData<List<UserEntity>> get() = _volunteers


    private var id : Long? = null
    private var name : String? = null

    fun saveName(savedId : Long, savedName : String) {
        id = savedId
        name = savedName
        updateState()
        Log.d("IdCenter", "$id")
        Log.d("NameCenter", "$name")
    }

    init {
        //if (id == null) _state.value = State.Error("ID центра не найден")
        //if (name == null) _state.value = State.Error("Имя центра не найдено")
        //else
            updateState()
    }

    fun clickRefresh() {
        updateState()
    }
    suspend fun isUserJoin() : Boolean {
        //Log.d("UserCenter", "${getUserUseCase.getUserFromStorage()?.center}")
        //Log.d("isUserJoin", "${getUserUseCase.getUserFromStorage()?.center == name}")
        getUserUseCase.invoke().fold(
            onSuccess = { data ->
                Log.d("uraa", "успех успех ${data.toString()}")
                if (data.center == name) return true
                else return false

            },
            onFailure = { error ->
                Log.d("kaput", error.message.toString())
                return false
            }
        )
    }

    fun updateState() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            //delay(2_000)
            if (id == null) {
                _state.emit(State.Error("Центр не найден"))
            } else {
                _state.emit(
                    getCenterUseCase.invoke(id!!).fold(
                        onSuccess = { data ->
                            Log.d("uraa", "успех успех ${data.toString()}")
                            State.Show(data)
                        },
                        onFailure = { error ->
                            Log.d("kaput", error.message.toString())
                            State.Error(error.message.toString())
                        }
                    )
                )
            }
        }
    }

    fun resetToastMessage() {
        _toastMessage.value = false
    }
    fun resetUpdateProfile() {
        _updateProfile.value = false
    }

    suspend fun joinTheCenter() {
        joinCenterUseCase.invoke(name!!).fold(
            onSuccess = { data ->
                Log.d("join", "Пользователь присоединен")
                _toastMessage.value = true
                _updateProfile.value = true

            },
            onFailure = { error ->
                Log.d("join", error.message.toString())
            }
        )
        updateState()

        //CenterCardFragment.joinNotification()

    }


    sealed interface State {
        data object Loading: State
        data class Show(
            val item : CenterEntity
        ) : State
        data class Error(
            val text: String
        ) : State
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CenterCardViewModel(
                    getCenterUseCase = GetCenterUseCase(
                        repo = CenterRepoImpl(
                            centerNetworkDataSource = CenterNetworkDataSource()
                        )
                    ),
                    joinCenterUseCase = JoinCenterUseCase(
                        repo = CenterRepoImpl(
                            centerNetworkDataSource = CenterNetworkDataSource()
                        )
                    ),
                    getUserUseCase = GetUserUseCase(
                        repo = UserRepoImpl(
                            userNetworkDataSource = UserNetworkDataSource()
                        ),
                        authStorageDataSource = AuthStorageDataSource
                    )
                ) as T
            }
        }
    }
}