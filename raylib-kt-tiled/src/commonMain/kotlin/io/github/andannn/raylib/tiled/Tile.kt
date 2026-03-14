package io.github.andannn.raylib.tiled

import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Tile(
  @SerialName(value = "id")
  public val id: Double,
  @SerialName(value = "image")
  public val image: String,
  @SerialName(value = "imageheight")
  public val imageheight: Double,
  @SerialName(value = "imagewidth")
  public val imagewidth: Double,
)
