/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tileset(
    /** Hex-formatted color (#RRGGBB or #AARRGGBB) (optional) */
    @SerialName("backgroundcolor")
    val backgroundColor: String? = null,

    /** The class of the tileset (since 1.9, optional) */
    @SerialName("class")
    val className: String? = null,

    /** The number of tile columns in the tileset */
    @SerialName("columns")
    val columns: Int? = null,

    /** The fill mode to use when rendering tiles from this tileset (stretch (default) or preserve-aspect-fit) (since 1.9) */
    @SerialName("fillmode")
    val fillMode: String = "stretch",

    /** GID corresponding to the first tile in the set */
    @SerialName("firstgid")
    val firstGid: Int? = null,

// TODO:
//  /** (optional) */
//  @SerialName("grid")
//  val grid: Grid? = null,

    /** Image used for tiles in this set */
    @SerialName("image")
    val image: String? = null,

    /** Height of source image in pixels */
    @SerialName("imageheight")
    val imageHeight: Int? = null,

    /** Width of source image in pixels */
    @SerialName("imagewidth")
    val imageWidth: Int? = null,

    /** Buffer between image edge and first tile (pixels) */
    @SerialName("margin")
    val margin: Int = 0,

    /** Name given to this tileset */
    @SerialName("name")
    val name: String? = null,

    /** Alignment to use for tile objects (unspecified (default), topleft, top, topright, left, center, right, bottomleft, bottom or bottomright) (since 1.4) */
    @SerialName("objectalignment")
    val objectAlignment: ObjectAlignment = ObjectAlignment.UNSPECIFIED,

// TODO:
//  /** Array of Properties */
//  @SerialName("properties")
//  val properties: List<Property>? = null,

    /** The external file that contains this tilesets data */
    @SerialName("source")
    val source: String? = null,

    /** Spacing between adjacent tiles in image (pixels) */
    @SerialName("spacing")
    val spacing: Int = 0,

// TODO:
//  /** Array of Terrains (optional) */
//  @SerialName("terrains")
//  val terrains: List<Terrain>? = null,

    /** The number of tiles in this tileset */
    @SerialName("tilecount")
    val tileCount: Int? = null,

    /** The Tiled version used to save the file */
    @SerialName("tiledversion")
    val tiledVersion: String? = null,

    /** Maximum height of tiles in this set */
    @SerialName("tileheight")
    val tileHeight: Int? = null,

// TODO:
//  /** (optional) */
//  @SerialName("tileoffset")
//  val tileOffset: TileOffset? = null,

    /** The size to use when rendering tiles from this tileset on a tile layer (tile (default) or grid) (since 1.9) */
    @SerialName("tilerendersize")
    val tileRenderSize: String = "tile",

// TODO:
//  /** Array of Tiles (optional) */
//  @SerialName("tiles")
//  val tiles: List<Tile>? = null,

    /** Maximum width of tiles in this set */
    @SerialName("tilewidth")
    val tileWidth: Int? = null,

// TODO:
//  /** Allowed transformations (optional) */
//  @SerialName("transformations")
//  val transformations: Transformations? = null,

    /** Hex-formatted color (#RRGGBB) (optional) */
    @SerialName("transparentcolor")
    val transparentColor: String? = null,

    /** tileset (for tileset files, since 1.0) */
    @SerialName("type")
    val type: String = "tileset",

    /** The JSON format version (previously a number, saved as string since 1.6) */
    @SerialName("version")
    val version: String? = null,

// TODO:
//  /** Array of Wang sets (since 1.1.5) */
//  @SerialName("wangsets")
//  val wangSets: List<WangSet>? = null
) {
    fun requireFirstGid(): Int = firstGid ?: error("First gid should be specified.")
    fun requireTileCount(): Int = tileCount ?: error("tileCount should be specified.")
    fun requireImage(): String = image ?: error("image should be specified.")
    fun requireColumns(): Int = columns ?: error("columns should be specified.")
    fun requireTileWidth(): Int = tileWidth ?: error("tileWidth should be specified.")
    fun requireTileHeight(): Int = tileHeight ?: error("tileHeight should be specified.")
}