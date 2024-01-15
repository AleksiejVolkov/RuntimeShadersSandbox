package com.offmind.ringshaders.utils

import android.content.Context
import com.offmind.ringshaders.domain.LoadImageBitmapUseCase
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


suspend fun List<RawShaderProperty>.toShaderProperties(loadImageBitmapUseCase: LoadImageBitmapUseCase): List<ShaderProperty> {
    return this.map {
        when (it.type) {
            "float" -> {
                ShaderProperty.FloatProperty(
                    displayName = it.displayName,
                    name = it.name,
                    value = (it.defaultValue as DefaultValue.FloatValue).value
                )
            }

            "color" -> {
                ShaderProperty.ColorProperty(
                    displayName = it.displayName,
                    name = it.name,
                    valueA = (it.defaultValue as DefaultValue.ColorValue).value[0],
                    valueB = (it.defaultValue as DefaultValue.ColorValue).value[1],
                    valueC = (it.defaultValue as DefaultValue.ColorValue).value[2]
                )
            }

            "image" -> {
                ShaderProperty.ImageProperty(
                    displayName = it.displayName,
                    name = it.name,
                    image = loadImageBitmapUseCase.execute((it.defaultValue as DefaultValue.StringValue).value)!!
                )
            }

            else -> throw IllegalArgumentException("Unknown shader property type")
        }
    }
}