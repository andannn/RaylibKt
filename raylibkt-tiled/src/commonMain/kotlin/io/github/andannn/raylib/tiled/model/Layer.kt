/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import io.github.andannn.raylib.foundation.Color
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.tiled.util.toRaylibColor
import io.github.andannn.zip.gzipSource
import io.github.andannn.zip.zlibSource
import kotlinx.cinterop.CValue
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.readByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlin.io.encoding.Base64

@Serializable
sealed class Layer {
    /** The class of the layer (since 1.9, optional) */
    abstract val className: String?

    /** Incremental ID - unique across all layers */
    abstract val id: Int

    /** Whether layer is locked in the editor (default: false). (since 1.8.2) */
    abstract val locked: Boolean

    /** The blend mode to use when rendering the layer. (since 1.12) */
    abstract val mode: BlendMode

    /** Name assigned to this layer */
    abstract val name: String

    /** Horizontal layer offset in pixels (default: 0) */
    abstract val offsetX: Double

    /** Vertical layer offset in pixels (default: 0) */
    abstract val offsetY: Double

    /** Value between 0 and 1 (default: 1) */
    abstract val opacity: Double

    /** Horizontal parallax factor for this layer (default: 1). (since 1.5) */
    abstract val parallaxX: Double

    /** Vertical parallax factor for this layer (default: 1). (since 1.5) */
    abstract val parallaxY: Double

// TODO:
//  /** Array of Properties */
//  abstract val properties: List<Property>?

    /** Hex-formatted tint color (#RRGGBB or #AARRGGBB) (optional). */
    abstract val tintColor: String?

    /** Whether layer is shown or hidden in editor */
    abstract val visible: Boolean

    /** Horizontal layer offset in tiles. Always 0. */
    abstract val x: Int

    /** Vertical layer offset in tiles. Always 0. */
    abstract val y: Int

    val raylibTintColor: CValue<Color> by lazy {
        tintColor?.toRaylibColor() ?: WHITE
    }
}

@Serializable
@SerialName("tilelayer")
data class TileLayer(
// TODO:
//  /** Array of chunks (optional). tilelayer only. */
//  @SerialName("chunks")
//  val chunks: List<Chunk>? = null,

    /** zlib, gzip, zstd (since 1.3) or empty (default). */
    @SerialName("compression")
    val compression: String = "",

    /**
     * Array of unsigned int (GIDs) or base64-encoded data.
     */
    @SerialName("data")
    val data: JsonElement,

    /** csv (default) or base64. */
    @SerialName("encoding")
    val encoding: String = "csv",

    /** Row count. Same as map height for fixed-size maps. */
    @SerialName("height")
    val height: Int,

    /** Column count. Same as map width for fixed-size maps. */
    @SerialName("width")
    val width: Int,

    /** X coordinate where layer content starts (for infinite maps) */
    @SerialName("startx")
    val startX: Int? = null,

    /** Y coordinate where layer content starts (for infinite maps) */
    @SerialName("starty")
    val startY: Int? = null,

    @SerialName("class") override val className: String? = null,
    @SerialName("id") override val id: Int,
    @SerialName("locked") override val locked: Boolean = false,
    @SerialName("mode") override val mode: BlendMode = BlendMode.NORMAL,
    @SerialName("name") override val name: String,
    @SerialName("offsetx") override val offsetX: Double = 0.0,
    @SerialName("offsety") override val offsetY: Double = 0.0,
    @SerialName("opacity") override val opacity: Double = 1.0,
    @SerialName("parallaxx") override val parallaxX: Double = 1.0,
    @SerialName("parallaxy") override val parallaxY: Double = 1.0,
// TODO:
//  @SerialName("properties") override val properties: List<Property>? = null,
    @SerialName("tintcolor") override val tintColor: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("x") override val x: Int = 0,
    @SerialName("y") override val y: Int = 0
) : Layer() {
    val gidArray: Array<GID> by lazy {
        when (encoding) {
            "csv" -> {
                Array(data.jsonArray.size) { i ->
                    GID(data.jsonArray[i].jsonPrimitive.long.toUInt())
                }
            }

            "base64" -> {
                val base64String = data.jsonPrimitive.content.filter { !it.isWhitespace() }
                val bytes = Base64.decode(base64String)

                val decompressedBytes = when (compression) {
                    "" -> bytes
                    "zlib" -> Buffer().apply { write(bytes) }.zlibSource().buffered().use { it.readByteArray() }
                    "gzip" -> Buffer().apply { write(bytes) }.gzipSource().buffered().use { it.readByteArray() }
// TODO: decompress data
//                    "zstd" -> decompressZstd(compressedBytes)
                    else -> throw SerializationException("Unsupported compression: $compression")
                }

                val uintArray = UIntArray(decompressedBytes.size / 4) { i ->
                    val offset = i * 4
                    val b0 = decompressedBytes[offset].toUInt() and 0xFFu
                    val b1 = decompressedBytes[offset + 1].toUInt() and 0xFFu
                    val b2 = decompressedBytes[offset + 2].toUInt() and 0xFFu
                    val b3 = decompressedBytes[offset + 3].toUInt() and 0xFFu

                    b0 or (b1 shl 8) or (b2 shl 16) or (b3 shl 24)
                }
                Array(uintArray.size) { i ->
                    GID(uintArray[i])
                }
            }

            else -> throw SerializationException("Unknown encoding: $encoding")
        }
    }
}

@Serializable
@SerialName("objectgroup")
data class ObjectGroupLayer(
    /** topdown (default) or index. objectgroup only. */
    @SerialName("draworder")
    val drawOrder: String = "topdown",

    /** Array of objects. objectgroup only. */
    @SerialName("objects")
    val objects: List<Object>,

    @SerialName("class") override val className: String? = null,
    @SerialName("id") override val id: Int,
    @SerialName("locked") override val locked: Boolean = false,
    @SerialName("mode") override val mode: BlendMode = BlendMode.NORMAL,
    @SerialName("name") override val name: String,
    @SerialName("offsetx") override val offsetX: Double = 0.0,
    @SerialName("offsety") override val offsetY: Double = 0.0,
    @SerialName("opacity") override val opacity: Double = 1.0,
    @SerialName("parallaxx") override val parallaxX: Double = 1.0,
    @SerialName("parallaxy") override val parallaxY: Double = 1.0,
// TODO:
//  @SerialName("properties") override val properties: List<Property>? = null,
    @SerialName("tintcolor") override val tintColor: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("x") override val x: Int = 0,
    @SerialName("y") override val y: Int = 0
) : Layer()

// TODO: add unit test.
@Serializable
@SerialName("imagelayer")
data class ImageLayer(
    /** Image used by this layer. imagelayer only. */
    @SerialName("image")
    val image: String,

    /** Height of the image used by this layer. imagelayer only. (since 1.11.1) */
    @SerialName("imageheight")
    val imageHeight: Int? = null,

    /** Width of the image used by this layer. imagelayer only. (since 1.11.1) */
    @SerialName("imagewidth")
    val imageWidth: Int? = null,

    /** Whether the image drawn by this layer is repeated along the X axis. imagelayer only. (since 1.8) */
    @SerialName("repeatx")
    val repeatX: Boolean = false,

    /** Whether the image drawn by this layer is repeated along the Y axis. imagelayer only. (since 1.8) */
    @SerialName("repeaty")
    val repeatY: Boolean = false,

    /** Hex-formatted color (#RRGGBB) (optional). imagelayer only. */
    @SerialName("transparentcolor")
    val transparentColor: String? = null,

    @SerialName("class") override val className: String? = null,
    @SerialName("id") override val id: Int,
    @SerialName("locked") override val locked: Boolean = false,
    @SerialName("mode") override val mode: BlendMode = BlendMode.NORMAL,
    @SerialName("name") override val name: String,
    @SerialName("offsetx") override val offsetX: Double = 0.0,
    @SerialName("offsety") override val offsetY: Double = 0.0,
    @SerialName("opacity") override val opacity: Double = 1.0,
    @SerialName("parallaxx") override val parallaxX: Double = 1.0,
    @SerialName("parallaxy") override val parallaxY: Double = 1.0,
// TODO:
//  @SerialName("properties") override val properties: List<Property>? = null,
    @SerialName("tintcolor") override val tintColor: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("x") override val x: Int = 0,
    @SerialName("y") override val y: Int = 0
) : Layer()

@Serializable
@SerialName("group")
data class GroupLayer(
    /** Array of layers. group only. */
    @SerialName("layers")
    val layers: List<Layer>,

    @SerialName("class") override val className: String? = null,
    @SerialName("id") override val id: Int,
    @SerialName("locked") override val locked: Boolean = false,
    @SerialName("mode") override val mode: BlendMode = BlendMode.NORMAL,
    @SerialName("name") override val name: String,
    @SerialName("offsetx") override val offsetX: Double = 0.0,
    @SerialName("offsety") override val offsetY: Double = 0.0,
    @SerialName("opacity") override val opacity: Double = 1.0,
    @SerialName("parallaxx") override val parallaxX: Double = 1.0,
    @SerialName("parallaxy") override val parallaxY: Double = 1.0,
// TODO:
//  @SerialName("properties") override val properties: List<Property>? = null,
    @SerialName("tintcolor") override val tintColor: String? = null,
    @SerialName("visible") override val visible: Boolean = true,
    @SerialName("x") override val x: Int = 0,
    @SerialName("y") override val y: Int = 0
) : Layer()
