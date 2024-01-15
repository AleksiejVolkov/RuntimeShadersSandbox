package com.offmind.ringshaders.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class RawShaderPropertyDeserializer : JsonDeserializer<RawShaderProperty> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RawShaderProperty {
        val jsonObject = json.asJsonObject

        val type = jsonObject.get("type").asString
        val name = jsonObject.get("name").asString
        val displayName = jsonObject.get("displayName").asString
        val defaultValueJson = jsonObject.get("defaultValue")

        val defaultValue: DefaultValue = when (type) {
            "string" -> DefaultValue.StringValue(defaultValueJson.asString)
            "color" -> DefaultValue.ColorValue(context.deserialize(defaultValueJson, List::class.java))
            "float" -> DefaultValue.FloatValue(defaultValueJson.asFloat)
            else -> throw JsonParseException("Unknown type: $type")
        }

        return RawShaderProperty(type, name, displayName, defaultValue)
    }
}

sealed class DefaultValue {
    data class StringValue(val value: String) : DefaultValue()
    data class FloatValue(val value: Float) : DefaultValue()
    data class ColorValue(val value: List<Float>) : DefaultValue()
}

data class RawShaderProperty(
    val type: String,
    val name: String,
    val displayName: String,
    val defaultValue: DefaultValue
)

