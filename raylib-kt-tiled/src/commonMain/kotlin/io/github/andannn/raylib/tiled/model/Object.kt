/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object TiledObjectSerializer : JsonContentPolymorphicSerializer<Object>(Object::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Object> {
        val json = element.jsonObject
        return when {
            json.containsKey("template") -> TemplateObject.serializer()
            json.containsKey("point") -> PointObject.serializer()
            json.containsKey("ellipse") -> EllipseObject.serializer()
            json.containsKey("capsule") -> CapsuleObject.serializer()
            json.containsKey("polygon") -> PolygonObject.serializer()
            json.containsKey("polyline") -> PolylineObject.serializer()
            json.containsKey("gid") -> TileObject.serializer()
            json.containsKey("text") -> TextObject.serializer()
            else -> RectObject.serializer()
        }
    }
}

@Serializable(with = TiledObjectSerializer::class)
sealed interface Object {
    val id: Int
    val x: Double?
    val y: Double?
    val name: String
    val type: String?
    val visible: Boolean
    val opacity: Double
    // TODO: val properties: List<Property>?

    fun requireX() = x ?: error("x should be specified.")
    fun requireY() = y ?: error("x should be specified.")

    fun copyWith(x: Double, y: Double, id: Int) = when (this) {
        is CapsuleObject -> copy(x = x, y = y, id = id)
        is EllipseObject -> copy(x = x, y = y, id = id)
        is PointObject -> copy(x = x, y = y, id = id)
        is PolygonObject -> copy(x = x, y = y, id = id)
        is PolylineObject -> copy(x = x, y = y, id = id)
        is RectObject -> copy(x = x, y = y, id = id)
        is TemplateObject -> copy(x = x, y = y, id = id)
        is TextObject -> copy(x = x, y = y, id = id)
        is TileObject -> copy(x = x, y = y, id = id)
    }
}

@Serializable
data class RectObject(
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
data class PointObject(
    @SerialName("point") val point: Boolean = true,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
data class EllipseObject(
    @SerialName("ellipse") val ellipse: Boolean = true,
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
data class CapsuleObject(
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("capsule") val capsule: Boolean = true,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
data class PolygonObject(
    @SerialName("polygon") val polygon: List<Point>,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
data class PolylineObject(
    @SerialName("polyline") val polyline: List<Point>,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
data class TileObject(
    @SerialName("gid") val gid: Int,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object {
    val gidObj: GID by lazy {
        GID(gid.toUInt())
    }
}

@Serializable
data class TextObject(
    @SerialName("text") val text: Text,
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double? = null,
    @SerialName("y") override val y: Double? = null,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

// TODO: Add unit test
@Serializable
data class TemplateObject(
    @SerialName("template") val template: String,
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double,
    @SerialName("y") override val y: Double,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : Object

@Serializable
class Point(
    /** X coordinate in pixels */
    @SerialName("x")
    val x: Double,
    /** Y coordinate in pixels */
    @SerialName("y")
    val y: Double
)