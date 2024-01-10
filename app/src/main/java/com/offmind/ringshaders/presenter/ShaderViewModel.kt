package com.offmind.ringshaders.presenter

import android.graphics.RuntimeShader
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offmind.ringshaders.domain.LoadShaderCodeUseCase
import com.offmind.ringshaders.domain.LoadShadersListUseCase
import com.offmind.ringshaders.domain.LoadedShader
import com.offmind.ringshaders.model.ShaderProperty
import com.offmind.ringshaders.utils.toShaderProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShaderViewModel(
    loadShadersListUseCase: LoadShadersListUseCase,
    private val loadShaderCodeUseCase: LoadShaderCodeUseCase
) : ViewModel() {

    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState

    init {
        viewModelScope.launch {
            val shaders = loadShadersListUseCase.execute()
            availableShadersList.clear()
            availableShadersList.addAll(shaders)
            availableShadersList.firstOrNull()?.let {
                loadNewShader(it)
            }
        }
    }

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            is UserEvent.OnChangeBackgroundDim -> {
                setBackgroundDim(userEvent.newValue)
            }

            is UserEvent.OnSelectNewShader -> {
                loadNewShader(userEvent.newShader)
            }

            is UserEvent.OnShaderPropertyChanged -> {
                onShaderPropertyChanged(userEvent.key, userEvent.newValue)
            }
        }
    }

    private fun loadNewShader(loadedShader: LoadedShader) {
        viewModelScope.launch {
            _screenState.emit(
                screenState.value.copy(
                    shader = loadedShader,
                    shaderProperties = loadedShader.properties.toShaderProperties(),
                    compiledShader = loadShaderCodeUseCase.execute(loadedShader.file),
                )
            )
        }
    }

    private fun setBackgroundDim(newValue: Float) {
        _screenState.value = _screenState.value.copy(backgroundDim = newValue)
    }

    private fun onShaderPropertyChanged(propertyName: String, newValue: List<Float>) {
        val properties = _screenState.value.shaderProperties.toMutableList()
        val propertyIndex = properties.indexOfFirst { it.name == propertyName }
        if (propertyIndex != -1) {
            val updatedProperty = when (val property = properties[propertyIndex]) {
                is ShaderProperty.FloatProperty -> property.copy(value = newValue[0])
                is ShaderProperty.Float2Property -> property.copy(valueA = newValue[0], valueB = newValue[1])
                is ShaderProperty.Float3Property -> property.copy(
                    valueA = newValue[0],
                    valueB = newValue[1],
                    valueC = newValue[2]
                )

                is ShaderProperty.Float4Property -> property.copy(
                    valueA = newValue[0],
                    valueB = newValue[1],
                    valueC = newValue[2],
                    valueD = newValue[3]
                )

                is ShaderProperty.ColorProperty -> property.copy(
                    valueA = newValue[0],
                    valueB = newValue[1],
                    valueC = newValue[2]
                )
            }

            properties[propertyIndex] = updatedProperty
            _screenState.value = _screenState.value.copy(shaderProperties = properties)
        }
    }
}

data class ScreenState(
    val isLoading: Boolean = true,
    val shader: LoadedShader? = null,
    val compiledShader: RuntimeShader? = null,
    val shaderProperties: List<ShaderProperty> = emptyList(),
    val backgroundDim: Float = 0.5f
)

sealed class UserEvent {
    class OnChangeBackgroundDim(val newValue: Float) : UserEvent()
    class OnSelectNewShader(val newShader: LoadedShader) : UserEvent()
    class OnShaderPropertyChanged(val key: String, val newValue: List<Float>) : UserEvent()
}

val availableShadersList = mutableListOf<LoadedShader>()