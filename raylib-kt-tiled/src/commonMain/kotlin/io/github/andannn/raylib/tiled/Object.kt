package io.github.andannn.raylib.tiled

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Object(
  @SerialName(value = "gid")
  public val gid: Double,
  @SerialName(value = "height")
  public val height: Double,
  @SerialName(value = "id")
  public val id: Double,
  @SerialName(value = "name")
  public val name: String,
  @SerialName(value = "rotation")
  public val rotation: Double,
  @SerialName(value = "type")
  public val type: String,
  @SerialName(value = "visible")
  public val visible: Boolean,
  @SerialName(value = "width")
  public val width: Double,
  @SerialName(value = "x")
  public val x: Double,
  @SerialName(value = "y")
  public val y: Double,
)
