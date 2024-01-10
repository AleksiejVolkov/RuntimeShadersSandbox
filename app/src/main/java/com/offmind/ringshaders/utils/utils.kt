package com.offmind.ringshaders.utils

import android.content.Context
import com.offmind.ringshaders.domain.RawShaderProperty
import com.offmind.ringshaders.model.ShaderProperty
import java.io.IOException
import java.nio.charset.Charset

// Function to load JSON from the assets folder
fun loadFromAsset(context: Context, fileName: String): String? {
    val json: String?
    try {
        // Open the JSON file from assets
        context.assets.open(fileName).use { inputStream ->
            // Create a size of bytes of the file
            val size = inputStream.available()
            // Create a buffer with the size
            val buffer = ByteArray(size)
            // Read data from the inputStream into the buffer
            inputStream.read(buffer)
            // Create a JSON string
            json = String(buffer, Charset.forName("UTF-8"))
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    // Return the JSON string
    return json
}

fun List<RawShaderProperty>.toShaderProperties(): List<ShaderProperty> {
    return this.map {
        when (it.type) {
            "float" -> {
                ShaderProperty.FloatProperty(
                    displayName = it.displayName,
                    name = it.name,
                    value = it.defaultValue.first()
                )
            }

            "float2" -> {
                ShaderProperty.Float2Property(
                    displayName = it.displayName,
                    name = it.name,
                    valueA = it.defaultValue[0],
                    valueB = it.defaultValue[1]
                )
            }

            "float3" -> {
                ShaderProperty.Float3Property(
                    displayName = it.displayName,
                    name = it.name,
                    valueA = it.defaultValue[0],
                    valueB = it.defaultValue[1],
                    valueC = it.defaultValue[2]
                )
            }

            "float4" -> {
                ShaderProperty.Float4Property(
                    displayName = it.displayName,
                    name = it.name,
                    valueA = it.defaultValue[0],
                    valueB = it.defaultValue[1],
                    valueC = it.defaultValue[2],
                    valueD = it.defaultValue[3]
                )
            }

            "color" -> {
                ShaderProperty.ColorProperty(
                    displayName = it.displayName,
                    name = it.name,
                    valueA = it.defaultValue[0],
                    valueB = it.defaultValue[1],
                    valueC = it.defaultValue[2]
                )
            }

            else -> throw IllegalArgumentException("Unknown shader property type")
        }
    }
}