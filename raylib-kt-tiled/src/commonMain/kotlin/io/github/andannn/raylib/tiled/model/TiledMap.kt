/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TiledMap(
  /** Hex-formatted color (#RRGGBB or #AARRGGBB) (optional) */
  @SerialName("backgroundcolor")
  val backgroundColor: String? = null,

  /** The class of the map (since 1.9, optional) */
  @SerialName("class")
  val className: String? = null,

  /** The compression level to use for tile layer data (defaults to -1, which means to use the algorithm default) */
  @SerialName("compressionlevel")
  val compressionLevel: Int = -1,

  /** Number of tile rows */
  @SerialName("height")
  val height: Int,

  /** Length of the side of a hex tile in pixels (hexagonal maps only) */
  @SerialName("hexsidelength")
  val hexSideLength: Int? = null,

  /** Whether the map has infinite dimensions */
  @SerialName("infinite")
  val infinite: Boolean,

  /** Array of Layers */
  @SerialName("layers")
  val layers: List<Layer>,

  /** Auto-increments for each layer */
  @SerialName("nextlayerid")
  val nextLayerId: Int,

  /** Auto-increments for each placed object */
  @SerialName("nextobjectid")
  val nextObjectId: Int,

  /** orthogonal, isometric, oblique, staggered or hexagonal */
  @SerialName("orientation")
  val orientation: String,

  /** X coordinate of the parallax origin in pixels (since 1.8, default: 0) */
  @SerialName("parallaxoriginx")
  val parallaxOriginX: Double = 0.0,

  /** Y coordinate of the parallax origin in pixels (since 1.8, default: 0) */
  @SerialName("parallaxoriginy")
  val parallaxOriginY: Double = 0.0,

// TODO: add property column
//  /** Array of Properties */
//  @SerialName("properties")
//  val properties: List<Property>? = null,

  /** right-down (the default), right-up, left-down or left-up (currently only supported for orthogonal maps) */
  @SerialName("renderorder")
  val renderOrder: String = "right-down",

  /** X offset applied per tile row (oblique maps only) */
  @SerialName("skewx")
  val skewX: Int? = null,

  /** Y offset applied per tile column (oblique maps only) */
  @SerialName("skewy")
  val skewY: Int? = null,

  /** x or y (staggered / hexagonal maps only) */
  @SerialName("staggeraxis")
  val staggerAxis: String? = null,

  /** odd or even (staggered / hexagonal maps only) */
  @SerialName("staggerindex")
  val staggerIndex: String? = null,

  /** The Tiled version used to save the file */
  @SerialName("tiledversion")
  val tiledVersion: String,

  /** Map grid height */
  @SerialName("tileheight")
  val tileHeight: Int,

  /** Array of Tilesets */
  @SerialName("tilesets")
  val tilesets: List<Tileset>,

  /** Map grid width */
  @SerialName("tilewidth")
  val tileWidth: Int,

  /** map (since 1.0) */
  @SerialName("type")
  val type: String = "map",

  /** The JSON format version (previously a number, saved as string since 1.6) */
  @SerialName("version")
  val version: String,

  /** Number of tile columns */
  @SerialName("width")
  val width: Int
)