package ru.sicampus.bootcamp2025.ui.centerCard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.data.center.CenterNetworkDataSource
import ru.sicampus.bootcamp2025.data.center.CenterRepoImpl
import ru.sicampus.bootcamp2025.domain.center.CenterEntity
import ru.sicampus.bootcamp2025.domain.center.GetCenterUseCase
import ru.sicampus.bootcamp2025.domain.center.JoinCenterUseCase
import ru.sicampus.bootcamp2025.domain.user.UserEntity

class CenterCardViewModel(
    private val getCenterUseCase : GetCenterUseCase,
    private val joinCenterUseCase : JoinCenterUseCase
) : ViewModel(){
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

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

    fun updateState() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            delay(5_000)
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

    suspend fun joinTheCenter() {
        joinCenterUseCase.invoke(name!!).fold(
            onSuccess = { data ->
                Log.d("join", "Пользователь присоединен")
            },
            onFailure = { error ->
                Log.d("join", error.message.toString())
            }
        )
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
                    )
                ) as T
            }
        }
    }
}