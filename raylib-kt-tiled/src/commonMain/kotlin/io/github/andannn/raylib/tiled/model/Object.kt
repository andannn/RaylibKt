package io.github.andannn.raylib.tiled.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TiledObject(
    /** Incremental ID, unique across all objects */
    @SerialName("id")
    val id: Int,

    /** X coordinate in pixels */
    @SerialName("x")
    val x: Double,

    /** Y coordinate in pixels */
    @SerialName("y")
    val y: Double,

    /** Width in pixels. */
    @SerialName("width")
    val width: Double = 0.0,

    /** Height in pixels. */
    @SerialName("height")
    val height: Double = 0.0,

    /** String assigned to name field in editor */
    @SerialName("name")
    val name: String = "",

    /** The class of the object (was saved as class in 1.9, optional) */
    @SerialName("type")
    val type: String? = null,

    /** Whether object is shown in editor. */
    @SerialName("visible")
    val visible: Boolean = true,

    /** Angle in degrees clockwise */
    @SerialName("rotation")
    val rotation: Double = 0.0,

    /** The opacity of the object as a value from 0 to 1 (default: 1) */
    @SerialName("opacity")
    val opacity: Double = 1.0,

    /** Global tile ID, only if object represents a tile */
    @SerialName("gid")
    val gid: Long? = null,

    /** Used to mark an object as a point */
    @SerialName("point")
    val point: Boolean = false,

    /** Used to mark an object as an ellipse */
    @SerialName("ellipse")
    val ellipse: Boolean = false,

    /** Used to mark an object as a capsule */
    @SerialName("capsule")
    val capsule: Boolean = false,

    /** Array of Points, in case the object is a polygon */
    @SerialName("polygon")
    val polygon: List<Point>? = null,

    /** Array of Points, in case the object is a polyline */
    @SerialName("polyline")
    val polyline: List<Point>? = null,
// TODO: implement
//    /** Only used for text objects */
//    @SerialName("text")
//    val text: TiledText? = null,

    /** Reference to a template file, in case object is a template instance */
    @SerialName("template")
    val template: String? = null,

// TODO:
//  /** Array of Properties */
//  @SerialName("properties")
//  val properties: List<Property>? = null
)


@Serializable
class Point(
    /** X coordinate in pixels */
    @SerialName("x")
    val x: Double,
    /** Y coordinate in pixels */
    @SerialName("y")
    val y: Double
)