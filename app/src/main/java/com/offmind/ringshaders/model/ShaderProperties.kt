package com.offmind.ringshaders.model

import androidx.compose.ui.graphics.ImageBitmap

sealed class ShaderProperty(
    open val displayName: String,
    open val name: String
) {
    data class FloatProperty(
        override val displayName: String,
        override val name: String,
        val value: Float
    ) : ShaderProperty(name = name, displayName = displayName)

    data class Float2Property(
        override val displayName: String,
        override val name: String,
        val valueA: Float,
        val valueB: Float
    ) : ShaderProperty(name = name, displayName = displayName)

    data class Float3Property(
        override val displayName: String,
        override val name: String,
        val valueA: Float,
        val valueB: Float,
        val valueC: Float
    ) : ShaderProperty(name = name, displayName = displayName)

    data class Float4Property(
        override val displayName: String,
        override val name: String,
        val valueA: Float,
        val valueB: Float,
        val valueC: Float,
        val valueD: Float
    ) : ShaderProperty(name = name, displayName = displayName)

    data class ColorProperty(
        override val displayName: String,
        override val name: String,
        val valueA: Float,
        val valueB: Float,
        val valueC: Float,
    ) : ShaderProperty(name = name, displayName = displayName)

    data class ImageProperty(
        override val displayName: String,
        override val name: String,
        val image: ImageBitmap
    ) : ShaderProperty(name = name, displayName = displayName)
}