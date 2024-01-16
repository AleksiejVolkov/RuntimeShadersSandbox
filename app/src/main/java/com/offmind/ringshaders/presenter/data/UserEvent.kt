package com.offmind.ringshaders.presenter.data

import com.offmind.ringshaders.domain.LoadedShader

sealed class UserEvent {
    class OnChangeBackgroundDim(val newValue: Float) : UserEvent()
    class OnSelectNewShader(val newShader: LoadedShader) : UserEvent()
    class OnShaderPropertyChanged(val key: String, val newValue: List<Float>) : UserEvent()
    object OnClickExpand : UserEvent()
}