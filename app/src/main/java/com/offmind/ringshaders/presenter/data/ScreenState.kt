package com.offmind.ringshaders.presenter.data

import android.graphics.RuntimeShader
import com.offmind.ringshaders.domain.LoadedShader
import com.offmind.ringshaders.model.ShaderProperty

data class ScreenState(
    val isExpanded: Boolean = false,
    val isLoading: Boolean = true,
    val shader: LoadedShader? = null,
    val compiledShader: RuntimeShader? = null,
    val shaderProperties: List<ShaderProperty> = emptyList(),
    val backgroundDim: Float = 0.5f,
    val shaderList: List<LoadedShader> = emptyList()
)
