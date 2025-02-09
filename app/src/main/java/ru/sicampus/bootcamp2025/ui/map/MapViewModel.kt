package ru.sicampus.bootcamp2025.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.data.center.CenterNetworkDataSource
import ru.sicampus.bootcamp2025.data.center.CenterRepoImpl
import ru.sicampus.bootcamp2025.domain.center.CenterEntity
import ru.sicampus.bootcamp2025.domain.center.GetCentersUseCase


class MapViewModel(
    private val getCentersUseCase: GetCentersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        updateStateGet()
    }

    fun clickRefresh() {
        updateStateGet()
    }

    fun updateStateGet() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            _state.emit(
                getCentersUseCase.invoke().fold(
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
            //_state.emit(State.Error("о нет ошибка ошибка помогите"))
        }
    }

    sealed interface State {
        data object Loading: State
        data class Show(
            val items: List<CenterEntity>
        ) : State
        data class Error(
            val text: String
        ) : State
    }


    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MapViewModel(
                    getCentersUseCase = GetCentersUseCase(
                        repo = CenterRepoImpl(
                            centerNetworkDataSource = CenterNetworkDataSource()
                        )
                    )
                ) as T
            }
        }
    }
}