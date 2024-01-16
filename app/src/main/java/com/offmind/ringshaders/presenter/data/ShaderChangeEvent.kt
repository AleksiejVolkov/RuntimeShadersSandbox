package com.offmind.ringshaders.presenter.data

import com.offmind.ringshaders.domain.LoadedShader

sealed class ShaderChangeEvent {
    class OnSelectNewShader(val newShader: LoadedShader) : ShaderChangeEvent()
    class OnShaderPropertyChanged(val key: String, val newValue: List<Float>) : ShaderChangeEvent()
}