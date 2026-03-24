/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object PropertySerializer : JsonContentPolymorphicSerializer<Property>(Property::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Property> {
        val typeProperty = element.jsonObject.get("type")?.jsonPrimitive ?: error("no type in element $element.")
        return when(typeProperty.content) {
            "int" -> IntProperty.serializer()
            "float" -> FloatProperty.serializer()
            "bool" -> BooleanProperty.serializer()
            "string" -> StringProperty.serializer()
            else -> error("type not supported yet!!")
        }
    }
}

@Serializable(with = PropertySerializer::class)
sealed interface Property {
    /**
     * Name of the property
     */
    val name: String
    /**
     * Value of the property
     */
    val value: Any
}

@Serializable
data class StringProperty(
    @SerialName("name")
    override val name: String,
    @SerialName("value")
    override val value: String,
) : Property

@Serializable
data class BooleanProperty(
    @SerialName("name")
    override val name: String,
    @SerialName("value")
    override val value: Boolean
): Property

@Serializable
data class IntProperty(
    @SerialName("name")
    override val name: String,
    @SerialName("value")
    override val value: Int
): Property

@Serializable
data class FloatProperty(
    @SerialName("name")
    override val name: String,
    @SerialName("value")
    override val value: Float
): Property

// TODO: add property color, file, object, class.

//    /**
//     * Type of the property (string (default), int, float, bool, color, file, object or class (since 0.16, with color and file added in 0.17, object added in 1.4 and class added in 1.8))
//     */
//    val type: String
//    /**
//     * Name of the custom property type, when applicable (since 1.8)
//     */
//    val propertyType: String?