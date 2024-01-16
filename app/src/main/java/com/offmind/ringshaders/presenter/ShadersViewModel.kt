package com.offmind.ringshaders.presenter

import com.offmind.ringshaders.presenter.data.ScreenState
import com.offmind.ringshaders.presenter.data.ShaderChangeEvent
import com.offmind.ringshaders.presenter.data.ShaderState
import kotlinx.coroutines.flow.StateFlow

interface ShadersViewModel {

    fun getShaderState(): StateFlow<ShaderState>

    fun onShaderEvent(shaderEvent: ShaderChangeEvent)

}