package com.offmind.ringshaders.domain

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.offmind.ringshaders.utils.loadFromAsset
import kotlinx.coroutines.coroutineScope

class LoadShadersListUseCase(private val context: Context) {

    suspend fun execute(): List<LoadedShader> = coroutineScope {
        val jsonString = loadFromAsset(context = context, "shaders_list.json")
        val gson = Gson()
        val typeToken = object : TypeToken<List<LoadedShader>>() {}.type

        return@coroutineScope if (jsonString != null) {
            gson.fromJson(jsonString, typeToken)
        } else {
            emptyList()
        }
    }
}

data class LoadedShader(
    val name: String,
    val description: String,
    val file: String,
    val properties: List<RawShaderProperty>
)

data class RawShaderProperty(
    val type: String,
    val name: String,
    val displayName: String,
    val defaultValue: List<Float>
)