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

object TiledObjectSerializer : JsonContentPolymorphicSerializer<TiledObject>(TiledObject::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<TiledObject> {
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
sealed interface TiledObject {
    val id: Int
    val x: Double
    val y: Double
    val name: String
    val type: String?
    val visible: Boolean
    val opacity: Double
    // TODO: val properties: List<Property>?
}

@Serializable
data class RectObject(
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
) : TiledObject

@Serializable
data class PointObject(
    @SerialName("point") val point: Boolean = true,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double,
    @SerialName("y") override val y: Double,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : TiledObject

@Serializable
data class EllipseObject(
    @SerialName("ellipse") val ellipse: Boolean = true,
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
) : TiledObject

@Serializable
data class CapsuleObject(
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("capsule") val capsule: Boolean = true,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double,
    @SerialName("y") override val y: Double,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : TiledObject

@Serializable
data class PolygonObject(
    @SerialName("polygon") val polygon: List<Point>,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double,
    @SerialName("y") override val y: Double,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : TiledObject

@Serializable
data class PolylineObject(
    @SerialName("polyline") val polyline: List<Point>,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double,
    @SerialName("y") override val y: Double,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : TiledObject

@Serializable
data class TileObject(
    @SerialName("gid") val gid: Long,
    @SerialName("rotation") val rotation: Double = 0.0,
    @SerialName("id") override val id: Int,
    @SerialName("x") override val x: Double,
    @SerialName("y") override val y: Double,
    @SerialName("width") val width: Double = 0.0,
    @SerialName("height") val height: Double = 0.0,
    @SerialName("name") override val name: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("opacity") override val opacity: Double = 1.0
) : TiledObject

@Serializable
data class TextObject(
    @SerialName("text") val text: TiledText,
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
) : TiledObject

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
) : TiledObject
//@Serializable
//data class TiledObject(
//    /** Incremental ID, unique across all objects */
//    @SerialName("id")
//    val id: Int,
//
//    /** X coordinate in pixels */
//    @SerialName("x")
//    val x: Double,
//
//    /** Y coordinate in pixels */
//    @SerialName("y")
//    val y: Double,
//
//    /** Width in pixels. */
//    @SerialName("width")
//    val width: Double = 0.0,
//
//    /** Height in pixels. */
//    @SerialName("height")
//    val height: Double = 0.0,
//
//    /** String assigned to name field in editor */
//    @SerialName("name")
//    val name: String = "",
//
//    /** The class of the object (was saved as class in 1.9, optional) */
//    @SerialName("type")
//    val type: String? = null,
//
//    /** Whether object is shown in editor. */
//    @SerialName("visible")
//    val visible: Boolean = true,
//
//    /** Angle in degrees clockwise */
//    @SerialName("rotation")
//    val rotation: Double = 0.0,
//
//    /** The opacity of the object as a value from 0 to 1 (default: 1) */
//    @SerialName("opacity")
//    val opacity: Double = 1.0,
//
//    /** Global tile ID, only if object represents a tile */
//    @SerialName("gid")
//    val gid: Long? = null,
//
//    /** Used to mark an object as a point */
//    @SerialName("point")
//    val point: Boolean = false,
//
//    /** Used to mark an object as an ellipse */
//    @SerialName("ellipse")
//    val ellipse: Boolean = false,
//
//    /** Used to mark an object as a capsule */
//    @SerialName("capsule")
//    val capsule: Boolean = false,
//
//    /** Array of Points, in case the object is a polygon */
//    @SerialName("polygon")
//    val polygon: List<Point>? = null,
//
//    /** Array of Points, in case the object is a polyline */
//    @SerialName("polyline")
//    val polyline: List<Point>? = null,
//    /** Only used for text objects */
//    @SerialName("text")
//    val text: TiledText? = null,
//
//    /** Reference to a template file, in case object is a template instance */
//    @SerialName("template")
//    val template: String? = null,
//
//// TODO:
////  /** Array of Properties */
////  @SerialName("properties")
////  val properties: List<Property>? = null
//)

@Serializable
class Point(
    /** X coordinate in pixels */
    @SerialName("x")
    val x: Double,
    /** Y coordinate in pixels */
    @SerialName("y")
    val y: Double
)