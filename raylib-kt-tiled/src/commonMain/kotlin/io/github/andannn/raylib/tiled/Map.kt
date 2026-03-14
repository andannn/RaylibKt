package io.github.andannn.raylib.tiled

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Map(
  @SerialName(value = "backgroundcolor")
  public val backgroundcolor: String,
  @SerialName(value = "class")
  public val `class`: String? = null,
  @SerialName(value = "compressionlevel")
  public val compressionlevel: Double,
  @SerialName(value = "height")
  public val height: Double,
  @SerialName(value = "infinite")
  public val infinite: Boolean,
  @SerialName(value = "layers")
  public val layers: List<Layer>,
  @SerialName(value = "nextlayerid")
  public val nextlayerid: Double,
  @SerialName(value = "nextobjectid")
  public val nextobjectid: Double,
  @SerialName(value = "orientation")
  public val orientation: String,
  @SerialName(value = "parallaxoriginx")
  public val parallaxoriginx: Double,
  @SerialName(value = "parallaxoriginy")
  public val parallaxoriginy: Double,
  @SerialName(value = "renderorder")
  public val renderorder: String,
  @SerialName(value = "tiledversion")
  public val tiledversion: String,
  @SerialName(value = "tileheight")
  public val tileheight: Double,
  @SerialName(value = "tilesets")
  public val tilesets: List<Tileset>,
  @SerialName(value = "tilewidth")
  public val tilewidth: Double,
  @SerialName(value = "type")
  public val type: String,
  @SerialName(value = "version")
  public val version: String,
  @SerialName(value = "width")
  public val width: Double,
)
