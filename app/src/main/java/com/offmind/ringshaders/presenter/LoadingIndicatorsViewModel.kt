package com.offmind.ringshaders.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offmind.ringshaders.domain.LoadImageBitmapUseCase
import com.offmind.ringshaders.domain.LoadShaderCodeUseCase
import com.offmind.ringshaders.domain.LoadShadersListUseCase
import com.offmind.ringshaders.domain.LoadedShader
import com.offmind.ringshaders.model.ShaderProperty
import com.offmind.ringshaders.presenter.data.ShaderChangeEvent
import com.offmind.ringshaders.presenter.data.ShaderState
import com.offmind.ringshaders.utils.toShaderProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadingIndicatorsViewModel(
    loadShadersListUseCase: LoadShadersListUseCase,
    private val loadImageFromAssetBitmap: LoadImageBitmapUseCase,
    private val loadShaderCodeUseCase: LoadShaderCodeUseCase
) : ViewModel(), ShadersViewModel {

    private val _shaderState: MutableStateFlow<ShaderState> = MutableStateFlow(ShaderState())
    private val shaderState: StateFlow<ShaderState> = _shaderState

    init {
        viewModelScope.launch {
            loadShadersListUseCase.execute().let {
                if (it.isNotEmpty()) {
                    _shaderState.emit(shaderState.value.copy(shaderList = it))
                    loadNewShader(it.first())
                }
            }
        }
    }

    private fun loadNewShader(loadedShader: LoadedShader) {
        viewModelScope.launch {
            _shaderState.emit(
                shaderState.value.copy(
                    shader = loadedShader,
                    shaderProperties = loadedShader.properties.toShaderProperties(loadImageFromAssetBitmap),
                    compiledShader = loadShaderCodeUseCase.execute(loadedShader.file),
                )
            )
        }
    }

    private fun onShaderPropertyChanged(
        propertyName: String,
        newValue: List<Float> = emptyList(),
        newStringValue: String = ""
    ) {
        viewModelScope.launch {
            val properties = _shaderState.value.shaderProperties.toMutableList()
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

                    is ShaderProperty.ImageProperty -> {
                        loadImageFromAssetBitmap.execute(newStringValue)?.let {
                            property.copy(image = it)
                        } ?: property
                    }
                }

                properties[propertyIndex] = updatedProperty
                _shaderState.emit(_shaderState.value.copy(shaderProperties = properties))
            }
        }
    }

    override fun getShaderState(): StateFlow<ShaderState> = shaderState

    override fun onShaderEvent(shaderEvent: ShaderChangeEvent) {
        when (shaderEvent) {

            is ShaderChangeEvent.OnSelectNewShader -> {
                loadNewShader(shaderEvent.newShader)
            }

            is ShaderChangeEvent.OnShaderPropertyChanged -> {
                onShaderPropertyChanged(shaderEvent.key, shaderEvent.newValue)
            }
        }
    }
}