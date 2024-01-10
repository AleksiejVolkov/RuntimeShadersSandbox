package com.offmind.ringshaders.domain

import android.content.Context
import android.graphics.RuntimeShader
import com.offmind.ringshaders.utils.loadFromAsset
import kotlinx.coroutines.coroutineScope

class LoadShaderCodeUseCase(private val context: Context) {

    suspend fun execute(fileName: String): RuntimeShader = coroutineScope {
        val string = loadFromAsset(context = context, "shaders/${fileName}.txt")
        return@coroutineScope RuntimeShader(string ?: "")
    }
}
