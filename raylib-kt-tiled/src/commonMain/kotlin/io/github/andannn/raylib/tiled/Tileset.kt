package io.github.andannn.raylib.tiled

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Tileset(
  @SerialName(value = "columns")
  public val columns: Double,
  @SerialName(value = "firstgid")
  public val firstgid: Double,
  @SerialName(value = "margin")
  public val margin: Double,
  @SerialName(value = "name")
  public val name: String,
  @SerialName(value = "spacing")
  public val spacing: Double,
  @SerialName(value = "tilecount")
  public val tilecount: Double,
  @SerialName(value = "tileheight")
  public val tileheight: Double,
  @SerialName(value = "tiles")
  public val tiles: List<Tile>,
  @SerialName(value = "tilewidth")
  public val tilewidth: Double,
)
