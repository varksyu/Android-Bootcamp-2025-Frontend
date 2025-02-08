package ru.sicampus.bootcamp2025.ui.centerCard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.domain.center.CenterEntity
import ru.sicampus.bootcamp2025.domain.user.UserEntity
import ru.sicampus.bootcamp2025.ui.profile.ProfileViewModel.State

class CenterCardViewModel : ViewModel(){
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        updateState()
    }

    fun clickRefresh() {
        updateState()
    }
    fun clickClose() {
        TODO("")
    }
    fun updateState() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            /*_state.emit(
                getCenterUseCase.getCenter().fold(
                    onSuccess = { data ->
                        Log.d("uraa", "успех успех ${data.toString()}")
                        ru.sicampus.bootcamp2025.ui.profile.ProfileViewModel.State.Show(data)
                    },
                    onFailure = { error ->
                        Log.d("kaput", error.message.toString())
                        ru.sicampus.bootcamp2025.ui.profile.ProfileViewModel.State.Error(error.message.toString())
                    }
                )*/

            //_state.emit(State.Error("о нет ошибка ошибка помогите"))
        }
    }

    fun joinTheCenter() {
        TODO("Not yet implemented")
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
}